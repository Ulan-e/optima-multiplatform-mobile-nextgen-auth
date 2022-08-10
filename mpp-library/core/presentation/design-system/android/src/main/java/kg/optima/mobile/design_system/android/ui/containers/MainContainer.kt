package kg.optima.mobile.design_system.android.ui.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kg.optima.mobile.design_system.android.ui.progressbars.CircularProgress

@Composable
fun MainContainer(
	topBar: @Composable () -> Unit = {},
	status: Status = Status.Initial,
	content: @Composable () -> Unit,
) {
	Scaffold(
		topBar = topBar
	) { padding ->
		padding
		Box(modifier = Modifier.fillMaxSize()) {
			content()

			when (status) {
				Status.Initial -> {}
				Status.Loading -> CircularProgress(Modifier.align(Alignment.Center))
			}
		}
	}
}