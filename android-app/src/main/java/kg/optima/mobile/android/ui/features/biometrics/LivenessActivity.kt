package kg.optima.mobile.android.ui.features.biometrics

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.google.accompanist.insets.ProvideWindowInsets
import kg.optima.mobile.design_system.android.theme.Theme
import kz.verigram.verilive.sdk.LivenessInitializer

class LivenessActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProvideWindowInsets {
                Theme.OptimaTheme {
                    BottomSheetNavigator(
                        sheetElevation = 0.dp,
                        sheetBackgroundColor = Color.Transparent,
                        sheetShape = RoundedCornerShape(16.dp, 16.dp),
                        content = {
                            LivenessInitializer.init()
                            Navigator(LivenessScreen)
                        },
                    )
                }
            }
        }
    }
}