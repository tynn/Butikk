//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.CompletableDeferred

/**
 * Await [State] of store matching [select].
 *
 * @param select The predicate [Selector] of the [State].
 */
suspend inline fun <State> Store<State>.await(
    crossinline select: Selector<State, Boolean>
) = with(CompletableDeferred<State>()) {
    subscribe {
        if (it.select()) {
            complete(it)
            unsubscribe()
        }
    }
    await()
}
