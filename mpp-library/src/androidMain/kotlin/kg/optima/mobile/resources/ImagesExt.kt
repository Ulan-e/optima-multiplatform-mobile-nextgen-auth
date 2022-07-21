package kg.optima.mobile.resources

import dev.icerock.moko.resources.ImageResource

public fun ImageResource?.resId(default: Int): Int {
	return this?.drawableResId ?: default
}

public fun ImageResource?.resId(): Int? {
	return this?.drawableResId
}