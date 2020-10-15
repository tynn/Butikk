//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Observe changes to [Value] in [State] of [Store.value].
 *
 * The function resumes when the store is closed or the [Observer] canceled.
 *
 * @param select The [Selector] of the [Value] in [State].
 * @param observe The [Observer] receiving the values.
 */
@Deprecated(
    "Store implements Flow and provides all its operators",
    ReplaceWith(
        "this.map(select).distinctUntilChanged().collect(observe)",
        "kotlinx.coroutines.flow.collect",
        "kotlinx.coroutines.flow.distinctUntilChanged",
        "kotlinx.coroutines.flow.map"
    )
)
suspend inline fun <State, Value> Store<State>.observe(
    crossinline select: Selector<State, Value>,
    crossinline observe: Observer<Value>
) = map {
    select(it)
}.distinctUntilChanged().collect {
    observe(it)
}
