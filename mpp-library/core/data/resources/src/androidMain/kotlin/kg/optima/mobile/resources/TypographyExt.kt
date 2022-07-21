package kg.optima.mobile.resources

import android.content.res.Resources
import android.util.TypedValue
import dev.icerock.moko.resources.FontResource
import kotlin.math.roundToInt

public fun FontResource.resId(): Int = this.fontResourceId

/**
 * DP to Px
 **/
public fun Int.toPx(): Int =
	TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		this.toFloat(),
		Resources.getSystem().displayMetrics
	).roundToInt()

/**
 * Px to Dp
 **/
public fun Int.toDp(): Int =
	TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_PX,
		this.toFloat(),
		Resources.getSystem().displayMetrics
	).roundToInt()
