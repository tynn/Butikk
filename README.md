# Butikk
[![Build][travis-shield]][travis]
[![Download][bintray-shield]][bintray]
[![Coverage][codecov-shield]][codecov]

###### _Kotlin Coroutines_ based `Store` implementation

_Butikk_ is a sequential store containing a single value. All sequential
updates are enforced via _Kotlin Coroutines_ and all operations are suspending.

Each update **must** be atomic, non-blocking and never throw.

## Usage

Create a store with a `CoroutineScope` and `CoroutineContext`

    val store = scope.createStore(newSingleThreadContext("store")) {
        loadState()
    }

A single thread context might have an advantage, but any other `Dispatcher`
would suffice as well.

The `Store` implements `Flow` and supports all operators available for these.

    store.collect { ... }


## License

    Copyright (C) 2019 Christian Schmitz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


  [bintray]: https://bintray.com/tynn-xyz/maven/Butikk/_latestVersion
  [bintray-shield]: https://api.bintray.com/packages/tynn-xyz/maven/Butikk/images/download.svg
  [codecov]: https://codecov.io/gh/tynn-xyz/Butikk
  [codecov-shield]: https://codecov.io/gh/tynn-xyz/Butikk/badge.svg
  [travis]: https://travis-ci.com/tynn-xyz/Butikk
  [travis-shield]: https://travis-ci.com/tynn-xyz/Butikk.svg
