package kg.optima.mobile.android.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity
import kg.optima.mobile.android.ui.base.BaseActivity

fun Context.asActivity(): FragmentActivity? = when (this) {
	is FragmentActivity -> this
	is ContextWrapper -> baseContext.asActivity()
	else -> null
}

fun Context.asBaseActivity(): BaseActivity? = when (this) {
	is BaseActivity -> this
	is ContextWrapper -> baseContext.asBaseActivity()
	else -> null
}