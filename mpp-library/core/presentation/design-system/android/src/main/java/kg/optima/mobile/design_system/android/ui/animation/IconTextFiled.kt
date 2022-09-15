package kg.optima.mobile.design_system.android.ui.animation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings

@Composable
fun IconTextFiled(resId: Int, text: String) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Icon(
			modifier = Modifier.size(Deps.Size.mainButtonImageSize),
			painter = painterResource(id = resId),
			contentDescription = null
		)
		Spacer(modifier = Modifier.width(Deps.Spacing.standardPadding))
		Text(
			text = text,
			fontSize = Headings.H4.sp,
		)
	}
}