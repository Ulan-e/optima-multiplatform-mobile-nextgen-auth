package kg.optima.mobile.design_system.android.ui.toolbars

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors

@Composable
fun MainToolbar(
	toolbarInfo: ToolbarInfo
) {
	TopAppBar(
		modifier = Modifier.fillMaxWidth(),
		title = {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center
			) {
				toolbarInfo.content.view()
			}
		},
		navigationIcon = {
			if (toolbarInfo.navigationIcon?.resId != null) {
				IconButton(
					onClick = toolbarInfo.navigationIcon.onBackClick
				) {
					Icon(
						painter = painterResource(id = toolbarInfo.navigationIcon.resId),
						contentDescription = null,
						tint = ComposeColors.PrimaryDisabledGray,
					)
				}
			}
		},
		actions = {
			toolbarInfo.actionIcons?.forEach {
				IconButton(onClick = { it.onClick.invoke() }) {
					Icon(
						painter = painterResource(id = it.id),
						contentDescription = null,
					)
				}
			} ?: Spacer(Modifier.width(64.dp))
		},
		backgroundColor = ComposeColors.Background,
		elevation = 0.dp,
	)
}