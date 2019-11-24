//  Copyright 2019 Christian Schmitz
//  SPDX-License-Identifier: Apache-2.0

package xyz.tynn.butikk.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance
import androidx.lifecycle.get
import xyz.tynn.butikk.example.R.layout.activity_example
import xyz.tynn.butikk.example.databinding.ActivityExampleBinding as Binding

class ExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = setContentView<Binding>(this, activity_example)
        binding.viewModel = ViewModelProvider(this, getInstance(application)).get()
        binding.lifecycleOwner = this
    }
}
