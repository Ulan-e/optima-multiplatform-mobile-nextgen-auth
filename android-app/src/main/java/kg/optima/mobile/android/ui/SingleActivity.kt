package kg.optima.mobile.android.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import com.google.accompanist.insets.ProvideWindowInsets
import kg.optima.mobile.android.ui.base.BaseActivity
import kg.optima.mobile.design_system.android.theme.Theme


class SingleActivity : BaseActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			ProvideWindowInsets {
				Theme.OptimaTheme {
					StartContent(nextScreenModel)
				}
			}
		}
	}

}
