package kg.optima.mobile.base.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

fun <T> Flow<T>.notNull() = this.filterNotNull()