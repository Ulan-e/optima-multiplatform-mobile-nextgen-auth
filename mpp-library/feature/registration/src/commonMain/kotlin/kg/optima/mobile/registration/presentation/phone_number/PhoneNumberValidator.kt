package kg.optima.mobile.registration.presentation.phone_number

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.core.error.Failure

object PhoneNumberValidator {
	fun validate(number: String): Either<Failure, Unit> {
		return when {
			number.length < Constants.PHONE_NUMBER_LENGTH ->
				Either.Left(Failure.Message("Заполните поле номера телефона"))
			number.length == Constants.PHONE_NUMBER_LENGTH ->
				Either.Right(Unit)
			else ->
				Either.Left(Failure.Message("Номер телефона неверный"))
		}
	}
}