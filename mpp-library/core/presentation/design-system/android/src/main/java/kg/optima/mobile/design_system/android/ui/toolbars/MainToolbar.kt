package kg.optima.mobile.design_system.android.ui.toolbars

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.optima.mobile.design_system.android.R
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.resources.Headings

@Composable
fun MainToolbar(
	modifier: Modifier = Modifier,
	text: String = "",
	onBackClick: () -> Unit = {},
	navigationIconId: Int? = R.drawable.ic_arrow_back,
	actionIcons: List<ActionIcon>? = null,
) {
	TopAppBar(
		title = {
			Text(
				text = text,
				fontSize = Headings.H4.px.sp,
				maxLines = 1,
				textAlign = TextAlign.Center,
				color = Color.Black,
				modifier = modifier.fillMaxWidth()
			)
		},
		navigationIcon = {
			if (navigationIconId != null) {
				IconButton(
					onClick = onBackClick
				) {
					Icon(
						painter = painterResource(id = navigationIconId),
						contentDescription = null,
						tint = ComposeColors.PrimaryDisabledGray,
					)
				}
			}
		},
		actions = {
			actionIcons?.forEach {
				IconButton(onClick = { it.onClick.invoke() }) {
					Icon(
						painter = painterResource(id = it.id),
						contentDescription = null,
					)
				}
			} ?: Spacer(Modifier.width(64.dp))
		},
		backgroundColor = ComposeColors.Background,
		elevation = 0.dp
	)
}