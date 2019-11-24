//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.runBlocking
import xyz.tynn.butikk.testing.GenericStoreUnitTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ActionKtTest : GenericStoreUnitTest<String>("init") {

    @Test
    fun `dispatch should provide current state`() = runBlocking {
        val value = "value"
        store.enqueue { value }

        store.dispatch {
            assertEquals(value, it)
        }
    }

    @Test
    fun `dispatch should interact with store`() = runBlocking {
        val values = collect<String> { store.subscribe(it) }
        val updates = listOf("update", "update", "end")

        store.dispatch {
            for (update in updates)
                enqueue { update }
        }

        assertEquals(listOf(initialState) + updates, values)
    }
}
