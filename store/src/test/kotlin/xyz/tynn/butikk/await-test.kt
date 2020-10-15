//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import xyz.tynn.butikk.testing.StoreUnitTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("EXPERIMENTAL_API_USAGE")
internal class AwaitKtTest : StoreUnitTest<String>("init") {

    @Test
    fun `await should return current value`() = runBlockingTest {
        assertEquals(initialState, store.await { true })
    }

    @Test
    fun `await should return selected value`() = runBlockingTest {
        val value = async {
            store.await(String::isBlank)
        }

        store.enqueue { "long" }
        store.enqueue { " " }

        assertEquals(" ", value.await())
    }

    @Test
    fun `cancel should cancel await`() = runBlockingTest {
        val launch = launch { store.await { throw CancellationException() } }

        launch.join()

        assertFalse { launch.isActive }
        assertTrue { launch.isCancelled }
    }

    @Test
    fun `cancel of context should cancel await`() = runBlockingTest {
        val launch = launch { store.await { false } }

        scope.cancel(CancellationException())
        launch.join()

        assertFalse { launch.isActive }
        assertTrue { launch.isCancelled }
    }
}
