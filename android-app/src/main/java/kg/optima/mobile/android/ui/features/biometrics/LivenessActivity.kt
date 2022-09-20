package kg.optima.mobile.android.ui.features.biometrics

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.voyager.navigator.Navigator
import com.google.accompanist.insets.ProvideWindowInsets
import kg.optima.mobile.design_system.android.theme.Theme
import kz.verigram.verilive.sdk.LivenessInitializer

class LivenessActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			ProvideWindowInsets {
				Theme.OptimaTheme {
					LivenessInitializer.init()
					Navigator(LivenessScreen)
				}
			}
		}
	}
}