package kg.optima.mobile.registration.presentation.phone_number

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure

object PhoneNumberValidator {
	private const val PHONE_NUMBER_LENGTH = 9

	fun validate(number: String): Either<Failure, Unit> {
		return when {
			number.length < PHONE_NUMBER_LENGTH ->
				Either.Left(Failure.Message("Заполните поле номера телефона"))
			number.length == PHONE_NUMBER_LENGTH ->
				Either.Right(Unit)
			else ->
				Either.Left(Failure.Message("Номер телефона неверный"))
		}
	}
}