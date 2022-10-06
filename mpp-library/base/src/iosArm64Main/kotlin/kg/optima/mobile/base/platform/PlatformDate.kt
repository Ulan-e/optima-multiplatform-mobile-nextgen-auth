package kg.optima.mobile.base.platform

import kg.optima.mobile.base.utils.orEmpty
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.timeIntervalSince1970

actual object PlatformDate {

    private const val DATE_PATTERN = "E, dd MMM yyyy HH:mm:ss"

    actual fun convertToTimeMills(date: String): Long {
        return try {
            val dateFormatter = NSDateFormatter()

            dateFormatter.dateFormat = DATE_PATTERN
            (dateFormatter.dateFromString(date)?.timeIntervalSince1970.orEmpty()).toLong() * 1000
        } catch (e: Exception) {
            0
        }
    }

    actual fun getTimeMills(): String {
        return NSDate().timeIntervalSince1970.toLong().toString()
    }
}