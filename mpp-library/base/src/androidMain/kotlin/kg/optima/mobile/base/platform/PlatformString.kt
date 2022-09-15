package kg.optima.mobile.base.platform

actual fun format(format: String, vararg args: Any?): String =
	String.format(format, args)
