//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Lambda typealias to initialize the [Store] with a `State`.
 */
typealias Initializer<State> = suspend () -> State

/**
 * Lambda typealias to observe the `Value` of the [Store].
 */
typealias Observer<Value> = suspend (Value) -> Unit

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
     * Enqueues an [update] to be applied to the [value].
     *
     * The function might resume before the update was applied.
     */
    suspend fun enqueue(update: Updater<State>)

    /**
     * Subscribes to [observe] all [value] changes.
     *
     * The function resumes when the store is closed or the [Observer] canceled.
     */
    suspend fun subscribe(observe: Observer<State>)
}

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
    initialize: Initializer<State>
) : Store<State> {
    private val store = ConflatedBroadcastChannel<State>()
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

    override suspend fun subscribe(
        observe: Observer<State>
    ) = store.consumeEach { observe(it) }
}
