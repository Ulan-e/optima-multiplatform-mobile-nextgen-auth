package kg.optima.mobile.design_system.android.theme

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object ThemeState {
    var darkModeState : MutableState<Boolean> = mutableStateOf(false)
}

val isDarkTheme = ThemeState.darkModeState.value
