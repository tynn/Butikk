//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.flow.first

/**
 * Await [State] of store matching [select].
 *
 * @param select The predicate [Selector] of the [State].
 */
@Deprecated(
    "Store implements Flow and provides all its operators",
    ReplaceWith(
        "this.first(select)",
        "kotlinx.coroutines.flow.first"
    )
)
suspend inline fun <State> Store<State>.await(
    crossinline select: Selector<State, Boolean>
) = first { select(it) }
