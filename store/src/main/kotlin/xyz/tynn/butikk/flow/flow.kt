//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk.flow

import kotlinx.coroutines.flow.Flow
import xyz.tynn.butikk.Store

/**
 * Consumes the store and creates a [Flow] of `State`.
 */
@Deprecated(
    "Store implements Flow and provides all its operators",
    ReplaceWith("this")
)
fun <State> Store<State>.asFlow(): Flow<State> = this
