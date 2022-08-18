package kg.optima.mobile.android.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity

fun Context.asActivity(): FragmentActivity? = when (this) {
	is FragmentActivity -> this
	is ContextWrapper -> baseContext.asActivity()
	else -> null
}