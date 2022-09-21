package kg.optima.mobile.registration.domain

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.component.RegistrationPreferences
import kg.optima.mobile.registration.data.model.otp_tries.OtpTriesModel
import kg.optima.mobile.registration.data.model.otp_tries.OtpTriesModelList
import kg.optima.mobile.registration.domain.model.OtpTriesEntity
import kg.optima.mobile.registration.presentation.sms_code.CheckSmsCodeInfo

class SaveTriesDataUseCase(
	private val registrationPreferences: RegistrationPreferences
) : BaseUseCase<SaveTriesDataUseCase.Params, CheckSmsCodeInfo>() {

	override suspend fun execute(model: Params): Either<Failure, CheckSmsCodeInfo> {
		val savingResult = mutableListOf<OtpTriesModel>()
		model.list.map {
			savingResult.add(
				OtpTriesModel(
					phoneNumber = it.phoneNumber,
					tryCount = it.tryCount,
					tryTime = it.tryTime
				)
			)
		}
		registrationPreferences.otpTriesModelList = OtpTriesModelList(savingResult)
		return Either.Right(
			CheckSmsCodeInfo.TryDataSaved
		)
	}

	class Params(
		val list : List<OtpTriesEntity>
	)

}