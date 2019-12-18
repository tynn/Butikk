//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.test.runBlockingTest
import xyz.tynn.butikk.testing.StoreUnitTest
import kotlin.coroutines.coroutineContext
import kotlin.test.*

@Suppress("EXPERIMENTAL_API_USAGE")
internal class StoreKtTest : StoreUnitTest<String>("init") {

    val name = CoroutineName("name")
    val error = "Store update failed"

    @Test
    fun `createStore should use coroutineContext of scope`() {
        val store = CoroutineScope(Unconfined + name).createStore {
            assertEquals(name, coroutineContext[CoroutineName])
            name
        }

        assertEquals(name, store.value)
    }

    @Test
    fun `createStore should use context`() {
        val store = CoroutineScope(Unconfined).createStore(name) {
            assertEquals(name, coroutineContext[CoroutineName])
            name
        }

        assertEquals(name, store.value)
    }

    @Test
    fun `initialize should initialize the state`() {
        assertEquals(initialState, store.value)
    }

    @Test
    fun `initialize should contain scope`() {
        val name = CoroutineName("name")
        val scope = scope + name

        store = scope.createStore {
            assertEquals(name, coroutineContext[CoroutineName])
            initialState
        }

        assertEquals(initialState, store.value)
    }

    @Test
    fun `initialize should contain scope and context`() {
        val name = CoroutineName("name")
        val scope = scope + CoroutineName("")

        store = scope.createStore(name) {
            assertEquals(name, coroutineContext[CoroutineName])
            initialState
        }

        assertEquals(initialState, store.value)
    }

    @Test
    fun `initialize should contain context`() {
        val name = CoroutineName("name")

        store = scope.createStore(name) {
            assertEquals(name, coroutineContext[CoroutineName])
            initialState
        }

        assertEquals(initialState, store.value)
    }

    @Test
    fun `initialize should cancel the store on error`() = runBlockingTest {
        store = scope.createStore { throw IllegalArgumentException("Error") }

        assertFailsWith<CancellationException>(error) { store.value }
        assertFailsWith<CancellationException>(error) { store.consume { } }
        assertFailsWith<CancellationException>(error) { store.enqueue { this } }
        Unit
    }

    @Test
    fun `value should throw before initialize`() {
        assertFailsWith<IllegalStateException> {
            scope.createStore { delay(1000) }.value
        }
    }

    @Test
    fun `cancel should cancel consume`() = runBlockingTest {
        val launch = launch { store.consume { cancel() } }

        launch.join()

        assertFalse { launch.isActive }
        assertTrue { launch.isCancelled }
    }

    @Test
    fun `cancel of context should cancel consume`() = runBlockingTest {
        val launch = launch { store.consume {} }

        scope.cancel(CancellationException())
        launch.join()

        assertFalse { launch.isActive }
        assertTrue { launch.isCancelled }
    }

    @Test
    fun `consume should observe all updates`() = runBlockingTest {
        val values = collect<String> { store.consume(it) }
        val updates = listOf("update", "update", "end")

        for (update in updates)
            store.enqueue { update }

        assertEquals(listOf(initialState) + updates, values)
    }

    @Test
    fun `enqueue should update the state`() = runBlockingTest {
        val update = "update"

        store.enqueue { update }

        assertEquals(update, store.value)
    }

    @Test
    fun `enqueue should cancel the store on error`() = runBlockingTest {
        store.enqueue { throw IllegalArgumentException("Error") }

        assertFailsWith<CancellationException>(error) { store.value }
        assertFailsWith<CancellationException>(error) { store.consume { } }
        assertFailsWith<CancellationException>(error) { store.enqueue { this } }
        Unit
    }
}
