//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

/**
 * Dispatches an [action] to run with the current [Store.value] provided as parameter.
 *
 * @param action The [Action] to run.
 */
suspend inline fun <State, Result> Store<State>.dispatch(
    crossinline action: Action<State, Result>
): Result = action(value)
