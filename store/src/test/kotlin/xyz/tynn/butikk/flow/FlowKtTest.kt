//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk.flow

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import xyz.tynn.butikk.testing.GenericStoreUnitTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class FlowKtTest : GenericStoreUnitTest<String>("init") {

    @Test
    fun `asFlow should observe all updates`() = runBlocking {
        val values = collect<String> { store.asFlow().collect(it) }
        val updates = listOf("update", "update", "end")

        for (update in updates)
            store.enqueue { update }

        assertEquals(listOf(initialState) + updates, values)
    }

    @Test
    fun `asFlow should start with latest state`() = runBlocking {
        val last = "end"

        for (update in listOf("update", "update", last))
            store.enqueue { update }

        assertEquals(listOf(last), collect { store.asFlow().collect(it) })
    }

    @Test
    fun `cancel of context should cancel the flow`() = runBlocking {
        val launch = launch(Unconfined) { store.asFlow().collect { } }
        val cause = CancellationException()

        scope.cancel(cause)

        assertFalse { launch.isActive }
        assertTrue { launch.isCancelled }
    }
}

