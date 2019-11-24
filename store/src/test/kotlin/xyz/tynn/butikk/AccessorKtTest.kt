//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import xyz.tynn.butikk.testing.GenericStoreUnitTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class AccessorKtTest : GenericStoreUnitTest<String>("init") {

    @Test
    fun `observe should observe changed values from state`() = runBlocking {
        val values = collect<Char> { store.observe(String::first, it) }

        for (update in listOf("update", "update", "end"))
            store.enqueue { update }

        assertEquals(listOf(initialState[0], 'u', 'e'), values)
    }

    @Test
    fun `cancel of context should cancel the observe`() = runBlocking {
        val launch = launch(Unconfined) { store.observe(String::first) {} }

        scope.cancel(CancellationException())

        assertFalse { launch.isActive }
        assertTrue { launch.isCancelled }
    }
}
