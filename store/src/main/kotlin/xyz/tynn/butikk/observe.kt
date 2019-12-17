//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

/**
 * Observe changes to [Value] in [State] of [Store.value].
 *
 * The function resumes when the store is closed or the [Observer] canceled.
 *
 * @param select The [Selector] of the [Value] in [State].
 * @param observe The [Observer] receiving the values.
 */
suspend inline fun <State, Value> Store<State>.observe(
    crossinline select: Selector<State, Value>,
    crossinline observe: Observer<Value>
) {
    var latest: Any? = Any()
    consume {
        val value = it.select()
        if (latest != value) {
            latest = value
            observe(value)
        }
    }
}
