//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk.testing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import xyz.tynn.butikk.Observer
import xyz.tynn.butikk.Store
import xyz.tynn.butikk.createStore
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class StoreUnitTest<T>(
    val initialState: T
) {

    lateinit var scope: CoroutineScope
    lateinit var store: Store<T>

    @BeforeTest
    fun initContext() {
        scope = CoroutineScope(Job() + Unconfined)
        store = scope.createStore { initialState }
    }

    @AfterTest
    fun cancelContext() {
        scope.cancel()
    }

    inline fun <T> collect(
        crossinline collector: suspend (Observer<T>) -> Unit
    ): List<T> = mutableListOf<T>().apply {
        scope.launch {
            collector { add(it) }
        }
    }
}

