package kg.optima.mobile.registration.presentation.sms_code.utils

enum class Timeouts(val timeout : Int) {
    FIRST(60),
    SECOND(180),
    THIRD(600),
    FOURTH(43200);

    companion object {
        fun get(tryCount : Int) = when (tryCount) {
            1 -> FIRST
            2 -> SECOND
            3 -> THIRD
            4 -> FOURTH
            else -> FIRST
        }
    }
}