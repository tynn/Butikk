@file:Suppress("SpellCheckingInspection")

object Deps {
    val androidx = Androidx
    val kotlin = Kotlin
    val kotlinx = Kotlinx

    object Androidx {
        const val archtesting = "androidx.arch.core:core-testing:2.1.0"
        const val appcompat = "androidx.appcompat:appcompat:1.2.0-alpha01"
        const val core = "androidx.core:core-ktx:1.2.0-rc01"
        val lifecycle = Lifecycle

        object Lifecycle {
            private const val group = "androidx.lifecycle"
            private const val version = "2.2.0-rc03"

            const val livedata = "$group:lifecycle-livedata-ktx:$version"
            const val viewmodel = "$group:lifecycle-viewmodel-ktx:$version"
        }
    }

    object Kotlin {
        private const val group = "org.jetbrains.kotlin"

        const val stdlib = "$group:kotlin-stdlib"
        val test = Test

        object Test {
            const val junit = "$group:kotlin-test-junit"
        }
    }

    object Kotlinx {
        private const val group = "org.jetbrains.kotlinx"

        val coroutines = Coroutines

        object Coroutines {
            private const val version = "1.3.0"

            const val core = "$group:kotlinx-coroutines-core:$version"
            const val test = "$group:kotlinx-coroutines-test:$version"
        }
    }
}
