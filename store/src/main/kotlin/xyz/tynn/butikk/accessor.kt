//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

/**
 * Lambda typealias to access `Value` in `State`.
 */
typealias Accessor<State, Value> = State.() -> Value

/**
 * Observe changes to [Value] in [State] of store.
 *
 * @param access The [Accessor] of the [Value] in [State].
 * @param observe The [Observer] receiving the values.
 */
suspend inline fun <State, Value> Store<State>.observe(
    crossinline access: Accessor<State, Value>,
    crossinline observe: Observer<Value>
) {
    var latest: Any? = Any()
    subscribe {
        val value = it.access()
        if (latest != value) {
            latest = value
            observe(value)
        }
    }
}
