//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import xyz.tynn.butikk.testing.GenericStoreUnitTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("EXPERIMENTAL_API_USAGE")
internal class ObserveKtTest : GenericStoreUnitTest<String>("init") {

    @Test
    fun `observe should observe changed values from state`() = runBlockingTest {
        val values = collect<Char> { store.observe(String::first, it) }

        for (update in listOf("update", "update", "end"))
            store.enqueue { update }

        assertEquals(listOf(initialState[0], 'u', 'e'), values)
    }

    @Test
    fun `cancel of context should cancel the observe`() = runBlockingTest {
        val launch = launch(Unconfined) { store.observe(String::first) {} }

        scope.cancel(CancellationException())

        assertFalse { launch.isActive }
        assertTrue { launch.isCancelled }
    }
}
