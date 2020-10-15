//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Creates a new [Store] with an initial value provided by [initialize].
 *
 * The coroutine context is inherited from a [CoroutineScope].
 * Additional context elements can be specified with [context] argument.
 *
 * @param context Additional to [CoroutineScope.coroutineContext] context of the store.
 * @param initialize The [Initializer] for the [Store.value].
 *
 * @see createStore for a factory not requiring a [CoroutineScope]
 */
fun <State> CoroutineScope.createStore(
    context: CoroutineContext = EmptyCoroutineContext,
    initialize: Initializer<State>
): Store<State> = StoreImpl(plus(context), initialize)

@Suppress("EXPERIMENTAL_API_USAGE")
private class StoreImpl<State>(
    scope: CoroutineScope,
    initialize: Initializer<State>,
    private val store: ConflatedBroadcastChannel<State> =
        ConflatedBroadcastChannel()
) : Store<State>, Flow<State> by store.asFlow() {

    private val dispatcher = scope.actor<Updater<State>> {
        try {
            store.send(initialize())
            for (update in channel)
                store.send(value.update())
        } catch (cause: Throwable) {
            CancellationException("Store update failed", cause).let {
                store.close(it)
                throw it
            }
        }
    }

    override val value get() = store.value

    override suspend fun enqueue(
        update: Updater<State>
    ) = dispatcher.send(update)
}
