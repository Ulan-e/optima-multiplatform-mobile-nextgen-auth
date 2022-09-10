package kg.optima.mobile.design_system.android.utils.resources

import android.content.res.Resources
import android.util.TypedValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.FontResource
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.Headings.Companion.pix
import kotlin.math.roundToInt

fun FontResource.resId(): Int = this.fontResourceId

/**
 * DP to Px
 **/
fun Int.toPx(): Int =
	TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		this.toFloat(),
		Resources.getSystem().displayMetrics
	).roundToInt()

/**
 * Px to Dp
 **/
fun Int.dp(): Int =
	TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_PX,
		this.toFloat(),
		Resources.getSystem().displayMetrics
	).roundToInt()

val Headings.sp: TextUnit
	get() = this.pix.dp().sp