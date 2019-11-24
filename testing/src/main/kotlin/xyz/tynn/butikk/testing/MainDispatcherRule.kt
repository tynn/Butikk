//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk.testing

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@Suppress("EXPERIMENTAL_API_USAGE")
class MainDispatcherRule(
    private val dispatcher: CoroutineDispatcher
) : TestWatcher() {

    override fun starting(
        description: Description?
    ) = Dispatchers.setMain(dispatcher)

    override fun finished(
        description: Description?
    ) = Dispatchers.resetMain()
}
