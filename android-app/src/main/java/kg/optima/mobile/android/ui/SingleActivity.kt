package kg.optima.mobile.android.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.google.accompanist.insets.ProvideWindowInsets
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.design_system.android.theme.Theme


class SingleActivity : AppCompatActivity() {

	companion object {
		const val NEXT_SCREEN_MODEL = "kg.optima.mobile.android.ui.SingleActivity:NextScreenModel"
	}

	@OptIn(ExperimentalMaterialApi::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val nextScreenModel = null//intent.getParcelableExtra<ScreenModel>(NEXT_SCREEN_MODEL)

		setContent {
			ProvideWindowInsets {
				Theme.OptimaTheme {
					BottomSheetNavigator(
						sheetElevation = 0.dp,
						sheetBackgroundColor = Color.Transparent,
						sheetShape = RoundedCornerShape(16.dp, 16.dp),
						content = {
							StartContent(nextScreenModel)
						},
					)
				}
			}
		}
	}

}
