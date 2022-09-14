package kg.optima.mobile.design_system.android.ui.text_fields

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.resources.Headings

@Composable
fun TitleTextField(
	modifier: Modifier = Modifier,
	text: String,
) {
	Text(
		modifier = modifier,
		text = text,
		fontSize = Headings.H2.sp,
		fontWeight = FontWeight.Bold,
	)
}