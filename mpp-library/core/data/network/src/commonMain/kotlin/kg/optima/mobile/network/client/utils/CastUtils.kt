package kg.optima.mobile.network.client.utils

/**
* Don`t use, not safety
* */
private fun <T> Any.cast(): T {
   return this as T
}

fun <T> Any.cast(defaultValue: T): T {
   return try {
	  this.cast()
   } catch (e: Exception) {
	  defaultValue
   }
}

fun <T> Any.castOrNull(): T? {
   return try {
	  this.cast()
   } catch (e: Exception) {
	  null
   }
}