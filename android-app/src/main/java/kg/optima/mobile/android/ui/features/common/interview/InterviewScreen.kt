package kg.optima.mobile.android.ui.features.common.interview

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.common.MainContainer
import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.design_system.android.ui.progressbars.CircularProgress
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.registration.RegistrationFeatureFactory
import kg.optima.mobile.registration.presentation.interview.InterviewIntent
import kg.optima.mobile.registration.presentation.interview.InterviewState
import kotlinx.parcelize.Parcelize

@Parcelize
class InterviewScreen(
	private val url: String
) : BaseScreen {

	@OptIn(ExperimentalMaterialApi::class)
	@Composable
	override fun Content() {
		val product = remember {
			RegistrationFeatureFactory.create<InterviewIntent, InterviewState>()
		}
		val intent = product.intent
		val state = product.state

		val model by state.stateFlow.collectAsState(initial = State.StateModel.Initial)
		val loadingState = remember { mutableStateOf(true) }

		MainContainer(
			mainState = model,
//			toolbarInfo = null,
			contentModifier = Modifier.fillMaxSize(),
			contentHorizontalAlignment = Alignment.Start,
		) {

			Box(modifier = Modifier.fillMaxSize()) {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.background(ComposeColors.Background),
					horizontalAlignment = Alignment.Start
				) {
					AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
						WebView(context).apply {
							layoutParams = ViewGroup.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT,
								ViewGroup.LayoutParams.MATCH_PARENT
							)
							settings.javaScriptEnabled = true
							settings.loadWithOverviewMode = true
							settings.useWideViewPort = true
							webViewClient = object : WebViewClient() {
								override fun onPageFinished(view: WebView?, url: String?) {
									super.onPageFinished(view, url)
									loadingState.value = false
								}
							}

							url?.let { it1 -> loadUrl(it1) }
//				LayoutInflater.from(context).inflate(R.layout.offer_layout, null, false).apply {
//						val imageClose = findViewById<ImageView>(R.id.iv_close)
//
//						imageClose.setOnClickListener { onBack() }
//
//						val webView: WebView = findViewById<WebView>(R.id.web_view).apply {
//							settings.javaScriptEnabled = true
//							settings.loadWithOverviewMode = true
//							settings.useWideViewPort = true
//							settings.builtInZoomControls = true
//							settings.displayZoomControls = true
//						}
//
//						webView.loadUrl(url)
//					}
						}
					}, update = {
						it.loadUrl(url)
					})
				}
				if (loadingState.value) { CircularProgress(modifier = Modifier.align(Alignment.Center)) }
			}


		}

	}
}