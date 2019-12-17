//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import xyz.tynn.butikk.testing.GenericStoreUnitTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("EXPERIMENTAL_API_USAGE")
internal class SubscribeKtTest : GenericStoreUnitTest<String>("init") {

    @Test
    fun `subscribe should observe changed values from state`() = runBlockingTest {
        val values = collect<String> { store.subscribe(it) }
        val updates = listOf("update", "update", "end")

        for (update in updates)
            store.enqueue { update }

        assertEquals(listOf(initialState) + updates, values)
    }

    @Test
    fun `unsubscribe should cancel the subscribe`() = runBlockingTest {
        store.subscribe { unsubscribe() }
    }

    @Test
    fun `unsubscribe should cancel any coroutine`() = runBlockingTest {
        val launch = launch { unsubscribe() }

        launch.join()

        assertFalse { launch.isActive }
        assertTrue { launch.isCancelled }
    }

    @Test
    fun `unsubscribe should cancel consume`() = runBlockingTest {
        val launch = launch { store.consume { unsubscribe() } }

        launch.join()

        assertFalse { launch.isActive }
        assertTrue { launch.isCancelled }
    }

    @Test
    fun `unsubscribe should cancel observe`() = runBlockingTest {
        val launch = launch { store.observe({ this }) { unsubscribe() } }

        launch.join()

        assertFalse { launch.isActive }
        assertTrue { launch.isCancelled }
    }

    @Test
    fun `cancel should cancel subscribe`() = runBlockingTest {
        val launch = launch { store.subscribe { cancel() } }

        launch.join()

        assertFalse { launch.isActive }
        assertTrue { launch.isCancelled }
    }

    @Test
    fun `cancel of context should cancel subscribe`() = runBlockingTest {
        val launch = launch { store.subscribe {} }

        scope.cancel(CancellationException())
        launch.join()

        assertFalse { launch.isActive }
        assertTrue { launch.isCancelled }
    }
}
