package kg.optima.mobile.android.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.google.accompanist.insets.ProvideWindowInsets
import kg.optima.mobile.android.ui.login.LoginScreen
import kg.optima.mobile.android.ui.main.MainScreen
import kg.optima.mobile.design_system.android.theme.Theme
import kotlinx.coroutines.CoroutineScope


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenResumed {
            setContent {
                ProvideWindowInsets {
                    Theme.OptimaTheme {
                        Content()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun Content() {
        BottomSheetNavigator(
            sheetElevation = 0.dp,
            sheetBackgroundColor = Color.Transparent,
            sheetShape = RoundedCornerShape(16.dp, 16.dp)
        ) {
            Navigator(screen = MainScreen)
        }
    }
}
