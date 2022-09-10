package kg.optima.mobile.design_system.android.ui.toolbars

import androidx.compose.ui.Modifier
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.R

class ToolbarInfo(
	val modifier: Modifier = Modifier,
	val navigationIcon: NavigationIcon? = NavigationIcon(),
	val content: ToolbarContent = ToolbarContent.Image(),
	val actionIcons: List<ActionIcon>? = null,
)

class NavigationIcon(
	val onBackClick: () -> Unit = {},
	val resId: Int = R.drawable.ic_arrow_back,
)