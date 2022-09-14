package kg.optima.mobile.android.ui.base

import android.os.Parcelable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import java.io.Serializable
import java.lang.System.currentTimeMillis

interface BaseScreen : Screen, Parcelable {
	override val key: ScreenKey
		get() = currentTimeMillis().toString()
}