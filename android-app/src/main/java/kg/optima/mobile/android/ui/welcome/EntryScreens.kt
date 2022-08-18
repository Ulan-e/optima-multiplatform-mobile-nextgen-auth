package kg.optima.mobile.android.ui.welcome

import androidx.fragment.app.FragmentActivity
import cafe.adriel.voyager.core.registry.ScreenProvider
import kg.optima.mobile.android.ui.RegisterScreens

sealed interface EntryScreens : ScreenProvider {
	class Welcome(
		val activity: FragmentActivity,
	) : EntryScreens
}

val entryScreens: RegisterScreens = {
	register<EntryScreens.Welcome> { WelcomeScreen(it.activity) }
}
