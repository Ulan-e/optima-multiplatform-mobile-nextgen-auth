package kg.optima.mobile.registration.data.repository

import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.registration.data.api.model.PhoneCheckDto


interface RegistrationRepository {
	suspend fun checkPhoneNumber(phoneNumber: String): Either<Failure, BaseDto<PhoneCheckDto>>
}