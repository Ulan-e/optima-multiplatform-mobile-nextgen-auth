package kg.optima.mobile.design_system.android.ui.text_fields

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.TextPaint
import android.text.style.*
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import kg.optima.mobile.design_system.android.R
import java.lang.Float.max

private const val SPACING_FIX = 3f

@Composable
fun HtmlText(
	modifier: Modifier = Modifier,
	text: String,
	textStyle: TextStyle = TextStyle.Default,
	onLinkClicked: ((String?) -> Unit) = {},
) {
	AndroidView(
		modifier = modifier,
		update = { it.text = fromHtml(it.context, text) },
		factory = { context ->
			val spacingReady =
				max(textStyle.lineHeight.value - textStyle.fontSize.value - SPACING_FIX, 0f)
			val extraSpacing = spToPx(spacingReady.toInt(), context)
			val gravity = when (textStyle.textAlign) {
				TextAlign.Center -> Gravity.CENTER
				TextAlign.End -> Gravity.END
				else -> Gravity.START
			}
			val fontResId = when (textStyle.fontWeight) {
				FontWeight.Medium -> R.font.ttnormspro_medium
				else -> R.font.ttnormspro_regular
			}
			val font = ResourcesCompat.getFont(context, fontResId)

			TextView(context).apply {
				textSize = textStyle.fontSize.value
				setLineSpacing(extraSpacing, 1f)
				setTextColor(textStyle.color.toArgb())
				setGravity(gravity)
				typeface = font

				setLinkTextColor(Color.RED)
				movementMethod = DefaultLinkMovementMethod { link ->
					onLinkClicked(link); true
				}
			}
		}
	)
}

private fun fromHtml(context: Context, html: String): Spannable = parse(html).apply {
	removeLinksUnderline()
	styleBold(context)
}

private fun parse(html: String): Spannable =
	(HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) as Spannable)

private fun Spannable.removeLinksUnderline() {
	for (s in getSpans(0, length, URLSpan::class.java)) {
		setSpan(object : UnderlineSpan() {
			override fun updateDrawState(tp: TextPaint) {
				tp.isUnderlineText = false
			}
		}, getSpanStart(s), getSpanEnd(s), 0)
	}
}

private fun Spannable.styleBold(context: Context) {
	val bold = ResourcesCompat.getFont(context, R.font.ttnormspro_medium)!!
	for (s in getSpans(0, length, StyleSpan::class.java)) {
		if (s.style == Typeface.BOLD) {
			setSpan(ForegroundColorSpan(Color.BLACK), getSpanStart(s), getSpanEnd(s), 0)
			setSpan(bold.getTypefaceSpan(), getSpanStart(s), getSpanEnd(s), 0)
		}
	}
}

private fun spToPx(sp: Int, context: Context): Float =
	TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_SP,
		sp.toFloat(),
		context.resources.displayMetrics
	)

private fun Typeface.getTypefaceSpan(): MetricAffectingSpan =
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
		typefaceSpanCompatV28(this)
	} else {
		CustomTypefaceSpan(this)
	}

@TargetApi(Build.VERSION_CODES.P)
private fun typefaceSpanCompatV28(typeface: Typeface) = TypefaceSpan(typeface)

private class CustomTypefaceSpan(private val typeface: Typeface?) : MetricAffectingSpan() {

	override fun updateDrawState(paint: TextPaint) {
		paint.typeface = typeface
	}

	override fun updateMeasureState(paint: TextPaint) {
		paint.typeface = typeface
	}
}