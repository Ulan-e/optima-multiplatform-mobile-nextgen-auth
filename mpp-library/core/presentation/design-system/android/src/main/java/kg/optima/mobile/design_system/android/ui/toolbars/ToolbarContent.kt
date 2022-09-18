package kg.optima.mobile.design_system.android.ui.toolbars

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.MainImages

sealed interface ToolbarContent {
	val view: @Composable () -> Unit
	val modifier: Modifier

	class Text(
		override val modifier: Modifier = Modifier,
		private val text: String,
	) : ToolbarContent {
		override val view: @Composable () -> Unit = {
			Text(
				modifier = modifier,
				text = text,
				fontSize = Headings.H4.px.sp,
				maxLines = 1,
				textAlign = TextAlign.Start,
				color = ComposeColors.PrimaryBlack,
			)
		}
	}

	class Image(
		override val modifier: Modifier = Modifier.size(width = 124.dp, height = 34.dp),
		private val imageId: Int = MainImages.optimaLogoOld.resId(),
	) : ToolbarContent {
		override val view: @Composable () -> Unit = {
			Image(
				modifier = modifier,
				painter = painterResource(id = imageId),
				alignment = Alignment.Center,
				contentDescription = "Optima24",
			)
		}
	}

	object Nothing : ToolbarContent {
		override val view: @Composable () -> Unit = {}
		override val modifier: Modifier = Modifier
	}
}