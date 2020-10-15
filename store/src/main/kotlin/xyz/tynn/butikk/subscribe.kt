//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

/**
 * Unsubscribe from within [subscribe].
 */
@Suppress("RedundantSuspendModifier")
@Deprecated("Store implements Flow and provides all its operators")
suspend fun unsubscribe(): Nothing = throw Unsubscribe

/**
 * Subscribe to consume all [Store.value] changes with [observe].
 *
 * The function resumes when the store is closed, the [Observer] canceled
 * or [unsubscribe] was called from within [observe].
 *
 * @param observe The [Observer] receiving the values.
 */
@Deprecated("Store implements Flow and provides all its operators")
suspend fun <State> Store<State>.subscribe(
    observe: Observer<State>
) = onEach {
    observe(it)
}.catch {
    if (it !is Unsubscribe)
        throw it
}.collect()

/**
 * Token exception thrown by [unsubscribe] and caught by [subscribe].
 */
private object Unsubscribe : CancellationException("called outside of subscribe")
