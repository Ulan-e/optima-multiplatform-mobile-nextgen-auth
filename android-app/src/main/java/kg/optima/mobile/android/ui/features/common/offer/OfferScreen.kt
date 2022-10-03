package kg.optima.mobile.android.ui.features.common.offer

import android.content.Intent
import android.view.LayoutInflater
import android.webkit.WebView
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import kg.optima.mobile.R
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.presentation.BaseMppState
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.offer.OfferIntent
import kg.optima.mobile.registration.presentation.offer.OfferState
import kotlinx.parcelize.Parcelize

@Parcelize
class OfferScreen(
	private val url: String
) : BaseScreen {

	@OptIn(ExperimentalMaterialApi::class)
	@Composable
	override fun Content() {
		val product = remember {
			RegistrationFeatureFactory.create<OfferIntent, OfferState>()
		}
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = BaseMppState.StateModel.Initial)

		MainContainer(
			mainState = model,
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