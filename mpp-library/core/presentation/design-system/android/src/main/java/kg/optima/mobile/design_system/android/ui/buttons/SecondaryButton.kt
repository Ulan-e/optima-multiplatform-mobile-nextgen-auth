package kg.optima.mobile.design_system.android.ui.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonSecondaryType
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings

@Composable
fun SecondaryButton(
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	text: String,
	fontSize: Headings = Headings.H4,
	buttonType: ButtonSecondaryType = ButtonSecondaryType.Main(),
	onClick: () -> Unit = {},
) {
	OutlinedButton(
		modifier = modifier
			.height(Deps.Size.buttonHeight)
			.background(
				color = Color.Companion.Transparent,
				shape = RoundedCornerShape(Deps.cornerRadius * 2)
			),
		border = BorderStroke(
			width = Deps.borderStroke,
			color = buttonType.composeColor.colorParameter.color
		),
		enabled = enabled,
		onClick = onClick,
	) {
		Text(
			text = text,
			fontSize = fontSize.sp,
			color = buttonType.composeColor.colorParameter.color,
		)
	}
}