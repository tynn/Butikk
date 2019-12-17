//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.test.runBlockingTest
import xyz.tynn.butikk.testing.GenericStoreUnitTest
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("EXPERIMENTAL_API_USAGE")
internal class ActionKtTest : GenericStoreUnitTest<String>("init") {

    @Test
    fun `dispatch should provide current state`() = runBlockingTest {
        val value = "value"
        store.enqueue { value }

        store.dispatch {
            assertEquals(value, it)
        }
    }

    @Test
    fun `dispatch should interact with store`() = runBlockingTest {
        val values = collect<String> { store.subscribe(it) }
        val updates = listOf("update", "update", "end")

        store.dispatch {
            for (update in updates)
                enqueue { update }
        }

        assertEquals(listOf(initialState) + updates, values)
    }
}
