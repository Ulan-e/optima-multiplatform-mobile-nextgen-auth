package kg.optima.mobile.base.platform

expect object PlatformDate {
	fun convertToTimeMills(date: String): Long

	fun getTimeMills(): String
}