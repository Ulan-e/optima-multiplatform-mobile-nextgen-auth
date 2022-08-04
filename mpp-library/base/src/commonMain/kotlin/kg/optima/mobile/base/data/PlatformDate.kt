package kg.optima.mobile.base.data

expect object PlatformDate {
    fun convertToTimeMills(date: String): Long
    fun getTimeMills(): String
}