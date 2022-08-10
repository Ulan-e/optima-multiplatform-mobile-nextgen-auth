package kg.optima.mobile.design_system.android.ui.text_fields

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.Headings.Companion.px
import kg.optima.mobile.design_system.android.utils.resources.sp

@Composable
fun TitleTextField(
	modifier: Modifier = Modifier,
	text: String,
) {
	Text(
		modifier = modifier,
		text = text,
		fontSize = Headings.H2.px().sp(),
		fontWeight = FontWeight.Bold,
	)
}