//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.rules.RuleChain.outerRule
import xyz.tynn.butikk.testing.GenericStoreUnitTest
import xyz.tynn.butikk.testing.MainDispatcherRule
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("EXPERIMENTAL_API_USAGE")
internal class LiveDataKtTest : GenericStoreUnitTest<String>("init") {

    @get:Rule
    val rule = outerRule(InstantTaskExecutorRule())
        .around(MainDispatcherRule(Unconfined))

    @Test
    fun `asLiveData should observe changed values from state`() = runBlockingTest {
        val liveData = store.asLiveData(String::length)

        val updates = listOf("update", "update", "end")
        val actual = collect<Int> { observe ->
            liveData.observeForever {
                runBlocking { observe(it) }
            }
        }

        for (update in updates)
            store.enqueue { update }

        val expected = linkedSetOf(initialState.length) + updates.map(String::length)
        assertEquals(expected.toList(), actual)
    }
}
