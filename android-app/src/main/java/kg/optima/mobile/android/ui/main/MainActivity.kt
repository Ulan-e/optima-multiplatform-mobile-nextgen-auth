package kg.optima.mobile.android.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.insets.ProvideWindowInsets
import kg.optima.mobile.design_system.android.theme.Theme
import kotlinx.coroutines.CoroutineScope


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenResumed {
            setContent {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    Theme.OptimaTheme {
                        Content(this)
                    }
                }
            }
        }
    }

    @Composable
    private fun Content(coroutineScope: CoroutineScope) {
        MainScreen()
    }
}
