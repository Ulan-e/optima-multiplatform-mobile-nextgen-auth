package kg.optima.mobile.registration.presentation.sms_code

sealed interface CheckSmsCodeInfo {

    class ReRequest(
        val timeout : Int
    ) : CheckSmsCodeInfo

    class Check(
        val success: Boolean,
        val smsCode: String,
    ) : CheckSmsCodeInfo



}