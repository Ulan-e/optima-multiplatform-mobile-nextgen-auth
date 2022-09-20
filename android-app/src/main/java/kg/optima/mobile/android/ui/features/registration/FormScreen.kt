package kg.optima.mobile.android.ui.features.registration

import android.view.LayoutInflater
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import kg.optima.mobile.R
import kg.optima.mobile.android.ui.base.BaseScreen
import kotlinx.parcelize.Parcelize


@Parcelize
class FormScreen(
    private val url: String
) : BaseScreen {

    @Composable
    override fun Content() {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                LayoutInflater.from(context).inflate(R.layout.offer_layout, null, false).apply {
                    val webView: WebView = rootView.findViewById<WebView>(R.id.web_view).apply {
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                    }

                    webView.loadUrl(url)
                }
            }
        )
    }
}