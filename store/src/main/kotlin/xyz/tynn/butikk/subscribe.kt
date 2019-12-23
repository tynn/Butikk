//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.CancellationException

/**
 * Unsubscribe from within [subscribe].
 */
@Suppress("RedundantSuspendModifier")
suspend fun unsubscribe(): Nothing = throw Unsubscribe

/**
 * Subscribe to consume all [Store.value] changes with [observe].
 *
 * The function resumes when the store is closed, the [Observer] canceled
 * or [unsubscribe] was called from within [observe].
 *
 * @param observe The [Observer] receiving the values.
 */
suspend fun <State> Store<State>.subscribe(
    observe: Observer<State>
) = try {
    consume(observe)
} catch (_: Unsubscribe) {
}

/**
 * Token exception thrown by [unsubscribe] and caught by [subscribe].
 */
private object Unsubscribe : CancellationException("called outside of subscribe")
