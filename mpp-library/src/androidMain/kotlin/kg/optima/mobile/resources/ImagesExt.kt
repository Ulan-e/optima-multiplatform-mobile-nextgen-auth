package kg.optima.mobile.resources

import dev.icerock.moko.resources.ImageResource
import kg.optima.mobile.R

fun ImageResource?.resId(default: Int = R.drawable.img_placeholder): Int {
	return this?.drawableResId ?: default
}