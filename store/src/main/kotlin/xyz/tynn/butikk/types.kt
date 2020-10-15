//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Lambda typealias to run an action on the `Store` with the current `State` as parameter.
 */
typealias Action<State, Result> = suspend Store<State>.(State) -> Result

/**
 * Lambda typealias to initialize the [Store] with a `State`.
 */
typealias Initializer<State> = suspend () -> State

/**
 * Lambda typealias to observe the `Value` of the [Store].
 */
@Deprecated("Store implements Flow and provides all its operators")
typealias Observer<Value> = suspend (Value) -> Unit

/**
 * Lambda typealias to select `Value` in `State`.
 */
@Deprecated("Store implements Flow and provides all its operators")
typealias Selector<State, Value> = State.() -> Value

/**
 * Lambda typealias to update the [Store] with a `State`.
 */
typealias Updater<State> = State.() -> State

/**
 * A store providing methods to update the [value] and observe changes to it.
 */
interface Store<State> : Flow<State> {

    /**
     * Gives access the current value of the store.
     */
    val value: State

    /**
     * Consumes all [value] changes with [observe].
     *
     * The function resumes when the store is closed or the [Observer] canceled.
     *
     * @param observe The [Observer] receiving the values.
     */
    @Deprecated(
        "Store implements Flow and provides all its operators",
        ReplaceWith(
            "this.collect(observe)",
            "kotlinx.coroutines.flow.collect"
        )
    )
    suspend fun consume(observe: Observer<State>) = collect { observe(it) }

    /**
     * Enqueues an [update] to be applied to the [value].
     *
     * The function might resume before the update was applied.
     *
     * @param update The [Updater] mutating the provided [value].
     */
    suspend fun enqueue(update: Updater<State>)
}
