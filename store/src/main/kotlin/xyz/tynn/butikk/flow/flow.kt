//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import xyz.tynn.butikk.Store

/**
 * Consumes the store and creates a [Flow] of `State`.
 */
fun <State> Store<State>.asFlow() = flow { consume { emit(it) } }
