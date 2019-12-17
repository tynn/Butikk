//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlin.coroutines.coroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals

class StoreKtTest {

    val name = CoroutineName("name")

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
}
