package kg.optima.mobile.images

import android.content.Context

fun String.resId(context: Context): Int {
	return try {
		context.resources.getIdentifier(this, "drawable", context.packageName)
	} catch (e: Exception) {
		this
		0
	}
}