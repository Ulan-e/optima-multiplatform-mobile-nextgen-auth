package kg.optima.mobile.design_system.android.utils.resources

import dev.icerock.moko.resources.ImageResource
import kg.optima.mobile.R

fun ImageResource?.resId(default: Int = R.drawable.img_placeholder): Int {
	return this?.drawableResId ?: default
}