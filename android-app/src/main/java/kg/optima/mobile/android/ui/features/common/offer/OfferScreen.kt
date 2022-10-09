package kg.optima.mobile.android.ui.features.common.offer

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.webkit.WebView
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import kg.optima.mobile.R
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.base.MainContainer
import kotlinx.parcelize.Parcelize

@Parcelize
class OfferScreen(
	private val url: String
) : BaseScreen {

	@SuppressLint("SetJavaScriptEnabled")
	@OptIn(ExperimentalMaterialApi::class)
	@Composable
	override fun Content() {
		MainContainer(
			mainState = null,
			toolbarInfo = null,
			contentModifier = Modifier.fillMaxSize(),
			contentHorizontalAlignment = Alignment.Start,
		) { onBack ->
			AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
				LayoutInflater.from(context).inflate(R.layout.offer_layout, null, false).apply {
					val imageClose = findViewById<ImageView>(R.id.iv_close)
					val imageShare = findViewById<ImageView>(R.id.iv_share)

					imageClose.setOnClickListener { onBack() }
					imageShare.setOnClickListener {
						val targetIntent = Intent(Intent.ACTION_SEND).apply {
							type = "text/plain"
							putExtra(Intent.EXTRA_TEXT, url)
						}
						val sendIntent = Intent.createChooser(targetIntent, "Поделиться")

						startActivity(context, sendIntent, null)
					}

					val webView: WebView = findViewById<WebView>(R.id.web_view).apply {
						settings.javaScriptEnabled = true
						settings.loadWithOverviewMode = true
						settings.useWideViewPort = true
						settings.builtInZoomControls = true
						settings.displayZoomControls = true
					}

					webView.loadUrl(url)
				}
			})
		}
	}

}