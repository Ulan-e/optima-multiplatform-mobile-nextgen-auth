package kg.optima.mobile.design_system.android.values

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kg.optima.mobile.resources.toDp

/**
 * Px to sp
 **/
fun Int.sp(): TextUnit = this.toDp().sp

/**
 * Px to dp
 **/
fun Int.dp(): Int = this.toDp()