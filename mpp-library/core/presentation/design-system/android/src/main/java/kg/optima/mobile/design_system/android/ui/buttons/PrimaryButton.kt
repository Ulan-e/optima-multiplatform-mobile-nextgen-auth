package kg.optima.mobile.design_system.android.ui.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings

@Composable
fun PrimaryButton(
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	text: String,
	fontSize: Headings = Headings.H4,
	color: Color = ComposeColors.PrimaryRed,
	onClick: () -> Unit = {},
) = Button(
	modifier = modifier
		.height(Deps.Size.buttonHeight)
		.background(color = Color.Companion.Transparent),
	shape = RoundedCornerShape(Deps.cornerRadius),
	colors = ButtonDefaults.buttonColors(
		backgroundColor = color,
		disabledBackgroundColor = ComposeColors.PrimaryDisabledGray,
	),
	onClick = onClick,
	enabled = enabled,
) {
	Text(
		text = text,
		fontSize = fontSize.sp,
		color = ComposeColors.PrimaryWhite,
	)
}