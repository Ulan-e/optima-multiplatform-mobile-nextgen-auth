package kg.optima.mobile.base.platform

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

actual object PlatformDate {

	private const val DATE_PATTERN = "E, dd MMM yyyy HH:mm:ss"

	actual fun convertToTimeMills(date: String): Long {
		return try {
			val inputFormat: DateFormat = SimpleDateFormat(DATE_PATTERN)
			val d: Date? = inputFormat.parse(date)
			d?.time ?: 0
		} catch (e: Exception) {
			0
		}
	}

	actual fun getTimeMills(): String {
		return System.currentTimeMillis().toString()
	}

}