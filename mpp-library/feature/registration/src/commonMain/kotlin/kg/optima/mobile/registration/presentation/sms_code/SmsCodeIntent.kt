package kg.optima.mobile.registration.presentation.sms_code

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.presentation.Intent
import kg.optima.mobile.registration.domain.CheckPhoneNumberUseCase
import kg.optima.mobile.registration.domain.CheckSmsCodeUseCase
import kg.optima.mobile.registration.domain.GetTriesDataUseCase
import kg.optima.mobile.registration.domain.model.SaveTriesDataUseCase
import kg.optima.mobile.registration.presentation.sms_code.utils.Timeouts
import kotlinx.coroutines.delay
import org.koin.core.component.inject

class SmsCodeIntent(
    override val state: SmsCodeState,
) : Intent<CheckSmsCodeInfo>() {

    private val checkSmsCodeUseCase: CheckSmsCodeUseCase by inject()
    private val checkPhoneNumberUseCase: CheckPhoneNumberUseCase by inject()
    private val getTriesDataUseCase: GetTriesDataUseCase by inject()
    private val saveTriesDataUseCase: SaveTriesDataUseCase by inject()

    fun smsCodeEntered(
        phoneNumber: String,
        smsCode: String,
        referenceId: String,
    ) {
        launchOperation {
            checkSmsCodeUseCase.execute(
                CheckSmsCodeUseCase.Params(
                    phoneNumber = phoneNumber,
                    verificationCode = smsCode,
                    referenceId = referenceId
                )
            ).map { CheckSmsCodeInfo.Check(it) }
        }
    }

    fun smsCodeReRequest(tryCount: Int, phoneNumber: String, currentTime: Long) {
        val newCount = if (tryCount >= 4) { 1 } else { tryCount + 1 }
        val newTimeLeft = Timeouts.get(tryCount + 1).timeout
        startTimeout(newTimeLeft)
        launchOperation {
            saveTriesDataUseCase.execute(SaveTriesDataUseCase.Params(
                phoneNumber = phoneNumber,
                tryCount =  newCount,
                tryTime = currentTime
            )).map {
                CheckSmsCodeInfo.TriesData(
                    tryCount = newCount,
                    timeLeft = newTimeLeft
                )
            }
        }
        launchOperation {
            checkPhoneNumberUseCase.execute(phoneNumber).map {
                CheckSmsCodeInfo.ReRequest(it.success)
            }
        }
    }

    fun startTimeout(timeout: Int) {
        var timer = timeout
        launchOperation {
            while (timer >= 0) {
                state.handle(CheckSmsCodeInfo.TimeLeft(timer))
                delay(1000)
                timer--
            }
            Either.Right(CheckSmsCodeInfo.EnableReRequest)
        }
    }

    fun getTriesData(currentPhoneNumber: String, currentTime: Long) {
        launchOperation {
            getTriesDataUseCase.execute(GetTriesDataUseCase.Params).map {
                if ((it.tryCount == 0) || (it.phoneNumber != currentPhoneNumber) || (((currentTime - it.tryTime) / 1000).toInt() >= Timeouts.FOURTH.timeout)) {
                    CheckSmsCodeInfo.TriesData(
                        it.tryCount + 1,
                        Timeouts.get(it.tryCount + 1).timeout
                    )
                } else {
                    CheckSmsCodeInfo.TriesData(
                        tryCount = it.tryCount,
                        timeLeft = if (((currentTime - it.tryTime) / 1000).toInt() <= Timeouts.get(it.tryCount).timeout) {
                            0
                        } else {
                            ((currentTime - it.tryTime) / 1000).toInt()
                        }
                    )
                }

            }
        }
    }

}