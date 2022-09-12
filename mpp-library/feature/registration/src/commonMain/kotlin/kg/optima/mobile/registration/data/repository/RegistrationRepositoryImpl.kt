package kg.optima.mobile.registration.data.repository

import kg.optima.mobile.base.data.BaseDataSource
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.api.RegistrationApi
import kg.optima.mobile.registration.data.api.model.PhoneCheckRequest


class RegistrationRepositoryImpl(
	private val registrationApi: RegistrationApi,
) : RegistrationRepository, BaseDataSource() {
	override suspend fun checkPhoneNumber(phoneNumber: String) = apiCall {
		registrationApi.checkPhoneNumber(PhoneCheckRequest(phoneNumber))
	}

	override suspend fun checkSmsCode(smsCode: String): Either<Failure, Boolean> {
		return Either.Right(true)
		//TODO: implement
	}

	override suspend fun reRequestSmsCode(): Either<Failure, Int> {
		return Either.Right(20)
		//TODO: implement
	}

}