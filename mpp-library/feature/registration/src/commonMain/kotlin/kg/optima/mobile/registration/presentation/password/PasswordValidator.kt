package kg.optima.mobile.registration.presentation.password

import kg.optima.mobile.core.common.Constants

object PasswordValidator {
	fun validate(password: String): List<PasswordValidityModel> {
		return mutableListOf(
			PasswordValidityModel(
				criteriaText = "Заглавные буквы",
				isValid = checkBigLetter(password)
			),
			PasswordValidityModel(
				criteriaText = "Цифры",
				isValid = checkNumber(password)
			),
			PasswordValidityModel(
				criteriaText = "Минимум 8 символов",
				isValid = (password.length >= Constants.PASSWORD_LENGTH)
			)
		)
	}

	private fun checkNumber(password: String): Boolean {
		var foundBigLetter = false
		for (char in password) {
			if (char.isDigit())
				foundBigLetter = true
		}
		return foundBigLetter
	}

	private fun checkBigLetter(password: String): Boolean {
		var foundBigLetter = false
		for (char in password) {
			if (char.isUpperCase())
				foundBigLetter = true
		}
		return foundBigLetter
	}

}