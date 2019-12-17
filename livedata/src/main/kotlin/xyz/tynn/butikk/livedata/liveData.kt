//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import xyz.tynn.butikk.Selector
import xyz.tynn.butikk.Store
import xyz.tynn.butikk.observe

/**
 * Subscribes to the store and creates a [LiveData] of `State`.
 */
fun <State> Store<State>.asLiveData() =
    liveData { subscribe { emit(it) } }

/**
 * Observes changes to the store and creates a [LiveData] of `Value`.
 */
inline fun <State, Value> Store<State>.asLiveData(
    crossinline select: Selector<State, Value>
) = liveData { observe(select) { emit(it) } }
