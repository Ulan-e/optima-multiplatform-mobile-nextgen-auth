package kg.optima.mobile.android.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.accompanist.insets.ProvideWindowInsets
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.design_system.android.theme.Theme


class SingleActivity : AppCompatActivity() {

	companion object {
		const val NEXT_SCREEN_MODEL = "kg.optima.mobile.android.ui.SingleActivity:NextScreenModel"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val nextScreenModel = intent.getParcelableExtra<ScreenModel>(NEXT_SCREEN_MODEL)

		setContent {
			ProvideWindowInsets {
				Theme.OptimaTheme {
					StartContent(nextScreenModel)
				}
			}
		}
	}

}
