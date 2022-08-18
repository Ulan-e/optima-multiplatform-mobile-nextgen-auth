package kg.optima.mobile.android.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import kg.optima.mobile.design_system.android.theme.Theme


class SingleActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			Theme.OptimaTheme {
				StartContent(this@SingleActivity)
			}
		}
	}
}
