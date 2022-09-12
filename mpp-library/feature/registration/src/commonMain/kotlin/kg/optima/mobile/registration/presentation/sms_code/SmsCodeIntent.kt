package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.registration.domain.CheckSmsCodeUseCase
import kg.optima.mobile.registration.domain.ReRequestSmsCodeUseCase
import org.koin.core.component.inject

class SmsCodeIntent(
    override val state: SmsCodeState,
) : Intent<CheckSmsCodeInfo>() {

    private val checkSmsCodeUseCase: CheckSmsCodeUseCase by inject()
    private val reRequestSmsCodeUseCase: ReRequestSmsCodeUseCase by inject()

    fun smsCodeEntered(smsCode: String) {
        launchOperation {
            checkSmsCodeUseCase.execute(smsCode).map {
                CheckSmsCodeInfo.Check(it, smsCode)
            }
        }
    }

    fun smsCodeReRequest() {
        launchOperation {
            reRequestSmsCodeUseCase.execute(Unit).map {
                CheckSmsCodeInfo.ReRequest(it)
            }
        }
    }

}