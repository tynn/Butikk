//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

/**
 * Lambda typealias to select `Value` in `State`.
 */
typealias Selector<State, Value> = State.() -> Value

/**
 * Observe changes to [Value] in [State] of store.
 *
 * @param select The [Selector] of the [Value] in [State].
 * @param observe The [Observer] receiving the values.
 */
suspend inline fun <State, Value> Store<State>.observe(
    crossinline select: Selector<State, Value>,
    crossinline observe: Observer<Value>
) {
    var latest: Any? = Any()
    subscribe {
        val value = it.select()
        if (latest != value) {
            latest = value
            observe(value)
        }
    }
}
