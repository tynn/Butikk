//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

@file:JvmName("LiveDataKt")

package xyz.tynn.butikk.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import xyz.tynn.butikk.Selector
import xyz.tynn.butikk.Store

/**
 * Observes changes to the store and creates a [LiveData] of `Value`.
 */
@Deprecated(
    "Store implements Flow and provides all its operators",
    ReplaceWith(
        "this.map(select).distinctUntilChanged().asLiveData()",
        "androidx.lifecycle.asLiveData",
        "kotlinx.coroutines.flow.distinctUntilChanged",
        "kotlinx.coroutines.flow.map"
    )
)
inline fun <State, Value> Store<State>.asLiveData(
    crossinline select: Selector<State, Value>
) = map {
    select(it)
}.distinctUntilChanged()
    .asLiveData()
