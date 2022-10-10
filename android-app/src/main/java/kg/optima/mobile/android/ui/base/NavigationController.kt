package kg.optima.mobile.android.ui.base

import android.os.Bundle
import android.os.Parcelable

class NavigationController(
	private val savedInstanceState: Bundle?
) {
	companion object {
		private const val STACK_SAVE_KEY =
			"kg.optima.mobile.android.ui.base.NavigationController:StackSaveKey"
	}

	private val stack: MutableMap<String, Any> = mutableMapOf()

	fun onSaveInstanceState(bundle: Bundle) {
		stack.forEach {
			when (val value = it.value) {
				is Parcelable -> bundle.putParcelable(it.key, value)
				is Boolean -> bundle.putBoolean(it.key, value)
			}
		}
		val keys = stack.map { it.key }.toTypedArray()
		bundle.putStringArray(STACK_SAVE_KEY, keys)
	}

	fun <T : Parcelable> set(key: String, value: T) {
		stack[key] = value as Parcelable
	}

	fun set(key: String, value: Boolean) {
		stack[key] = value
	}

	fun set(pairs: Map<String, Boolean>) {
		pairs.forEach {
			stack[it.key] = it.value
		}
	}

	@Suppress("UNCHECKED_CAST")
	fun <T : Parcelable> get(key: String) =
		stack[key] as? T ?: savedInstanceState?.getParcelable(key)

	@Suppress("UNCHECKED_CAST")
	fun get(key: String) =
		stack[key] as? Boolean ?: savedInstanceState?.getBoolean(key)
}