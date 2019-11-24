//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.tynn.butikk.Store

/**
 * Subscribes to the store and creates a [Flow] of `State`.
 */
fun <State> Store<State>.asFlow() = flow { subscribe { emit(it) } }
