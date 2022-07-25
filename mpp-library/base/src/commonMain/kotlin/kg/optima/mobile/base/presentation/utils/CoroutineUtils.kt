package kg.optima.mobile.base.presentation.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.native.concurrent.ThreadLocal


/**
 * Factory of viewModelScope. Internal API, for ability of mvvm-test to change viewModelScope
 * dispatcher.
 *
 * In default implementation create main-thread dispatcher scope.
 */
@ThreadLocal
var createCoroutineScope: (CoroutineDispatcher?) -> CoroutineScope = { dispatcher ->
    CoroutineScope((dispatcher ?: UIDispatcher) + SupervisorJob())
}

private val UIDispatcher: CoroutineDispatcher
    get() = Dispatchers.Main