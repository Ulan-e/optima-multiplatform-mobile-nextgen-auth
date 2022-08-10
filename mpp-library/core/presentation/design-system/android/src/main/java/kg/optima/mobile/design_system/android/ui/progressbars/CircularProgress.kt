package kg.optima.mobile.design_system.android.ui.progressbars

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors

@Composable
fun CircularProgress(modifier: Modifier) {
	CircularProgressIndicator(
		modifier = modifier,
		color = ComposeColors.PrimaryRed
	)
}