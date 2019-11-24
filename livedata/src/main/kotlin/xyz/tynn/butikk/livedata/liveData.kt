//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import xyz.tynn.butikk.Accessor
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
    crossinline access: Accessor<State, Value>
) = liveData { observe(access) { emit(it) } }
