//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

/**
 * Lambda typealias to run an action on the `Store` with the current `State` as parameter.
 */
typealias Action<State, Result> = suspend Store<State>.(State) -> Result

/**
 * Dispatches an [action] to run with the current [Store.value] provided as parameter.
 *
 * @param action The [Action] to run.
 */
suspend inline fun <State, Result> Store<State>.dispatch(
    crossinline action: Action<State, Result>
): Result = action(value)
