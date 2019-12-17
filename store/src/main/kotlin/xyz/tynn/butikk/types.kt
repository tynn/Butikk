//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

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
typealias Observer<Value> = suspend (Value) -> Unit

/**
 * Lambda typealias to select `Value` in `State`.
 */
typealias Selector<State, Value> = State.() -> Value

/**
 * Lambda typealias to update the [Store] with a `State`.
 */
typealias Updater<State> = State.() -> State

/**
 * A store providing methods to update the [value] and observe changes to it.
 */
interface Store<State> {

    /**
     * Gives access the current value of the store.
     */
    val value: State

    /**
     * Consumes all [value] changes with [observe].
     *
     * The function resumes when the store is closed or the [Observer] canceled.
     */
    suspend fun consume(observe: Observer<State>)

    /**
     * Enqueues an [update] to be applied to the [value].
     *
     * The function might resume before the update was applied.
     */
    suspend fun enqueue(update: Updater<State>)
}
