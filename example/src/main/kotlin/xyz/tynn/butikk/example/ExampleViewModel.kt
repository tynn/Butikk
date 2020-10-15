//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk.example

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import xyz.tynn.butikk.Store
import xyz.tynn.butikk.Updater
import xyz.tynn.butikk.createStore

class ExampleViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val prefs by lazy {
        application.getSharedPreferences("example_view_model", MODE_PRIVATE)
    }

    private val store by lazy {
        with(viewModelScope) {
            @Suppress("EXPERIMENTAL_API_USAGE")
            createStore(newSingleThreadContext("store")) {
                with(prefs) {
                    ExampleModel(
                        getLong(KEY_CURRENT, 0),
                        getLong(KEY_TOTAL, 0),
                        getLong(KEY_RESET, 0)
                    )
                }
            }.apply {
                launch {
                    collect {
                        Log.d("ExampleViewModel", it.toString())
                    }
                }
                launch {
                    distinctUntilChanged()
                        .collect {
                            prefs.edit {
                                putLong(KEY_CURRENT, it.currentCount)
                                putLong(KEY_TOTAL, it.totalCount)
                                putLong(KEY_RESET, it.resetCount)
                            }
                        }
                }
            }
        }
    }

    val counts by lazy {
        store.map {
            "${it.currentCount}/${it.resetCount}/${it.totalCount}"
        }.asLiveData()
    }

    fun incrementCount() = update(store) {
        copy(
            currentCount = currentCount + 1,
            totalCount = totalCount + 1
        )
    }

    fun repeatCount() = update(store) {
        this
    }

    fun resetCount() = update(store) {
        copy(
            currentCount = 0,
            resetCount = resetCount + 1
        )
    }

    private fun <State> ViewModel.update(
        store: Store<State>,
        update: Updater<State>
    ) {
        viewModelScope.launch {
            store.enqueue(update)
        }
    }

    private companion object {
        const val KEY_CURRENT = "count.current"
        const val KEY_RESET = "count.reset"
        const val KEY_TOTAL = "count.total"
    }
}
