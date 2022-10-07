package kg.optima.mobile.android.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.accompanist.insets.ProvideWindowInsets
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.design_system.android.theme.Theme


class SingleActivity : AppCompatActivity() {

	companion object {
		const val TARGET_NAVIGATE_MODEL = "kg.optima.mobile.android.ui.SingleActivity:TargetNavigateModel"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val nextScreenModel = intent.getParcelableExtra<UiState.Model.Navigate>(TARGET_NAVIGATE_MODEL)

		setContent {
			ProvideWindowInsets {
				Theme.OptimaTheme {
					StartContent(nextScreenModel)
				}
			}
		}
	}

}
