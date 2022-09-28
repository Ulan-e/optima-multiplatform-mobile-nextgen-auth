package kg.optima.mobile.design_system.android.ui.text_fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings

@Composable
fun AnnotatedText(
	text: String,
	underLineText: String,
	onClick: () -> Unit
) {
	val tag = "tagText"
	val annotatedString = buildAnnotatedString {
		val mainTextStart = text.indexOf(text)
		val mainTextEnd = mainTextStart + text.length

		val underLineTextStart = text.indexOf(underLineText)
		val underLineTextEnd = underLineTextStart + 16

		append(text)
		addStyle(
			style = SpanStyle(
				color = ComposeColors.PrimaryRed,
				fontSize = Headings.H5.sp,
				fontWeight = FontWeight.Bold,
			),
			start = underLineTextStart,
			end = underLineTextEnd,
		)

		addStringAnnotation(
			tag = tag,
			annotation = emptyString,
			start = mainTextStart,
			end = mainTextEnd
		)
	}

	Column(
		Modifier
			.fillMaxWidth()
			.padding(start = Deps.Spacing.standardMargin)
	) {
		ClickableText(
			text = annotatedString,
			style = LocalTextStyle.current.copy(
				color = ComposeColors.DarkGray,
				fontSize = Headings.H5.sp,
				fontWeight = FontWeight.Medium,
			),
			onClick = {
				annotatedString.getStringAnnotations(tag, it, it).firstOrNull()?.let {
					onClick()
				}
			}
		)
	}
}