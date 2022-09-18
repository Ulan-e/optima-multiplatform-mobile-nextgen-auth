package kg.optima.mobile.base.platform

import kg.optima.mobile.base.utils.emptyString
import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun format(format: String, vararg args: Any?): String {
    var returnString = emptyString
    val regEx = "%[\\d|.]*[sdf]|[%]".toRegex()
    val singleFormats = regEx.findAll(format).map { it.groupValues.first() }.asSequence().toList()
    val newStrings = format.split(regEx)
    args.forEachIndexed { i, arg ->
        returnString += when (arg) {
            is Double -> NSString.stringWithFormat(
                newStrings[i] + singleFormats[i],
                args[i] as Double
            )
            is Int -> NSString.stringWithFormat(newStrings[i] + singleFormats[i], args[i] as Int)
            else -> NSString.stringWithFormat(newStrings[i] + "%@", args[i])
        }
    }

    return returnString
}