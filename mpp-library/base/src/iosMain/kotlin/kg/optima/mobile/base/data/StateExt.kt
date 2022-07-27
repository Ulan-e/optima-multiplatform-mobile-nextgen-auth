package kg.optima.mobile.base.data

import kg.optima.mobile.base.presentation.utils.CommonFlow
import kg.optima.mobile.base.presentation.utils.asCommonFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

fun <T> Flow<T>.asCommonFlow(): CommonFlow<out T> = this.filterNotNull().asCommonFlow()