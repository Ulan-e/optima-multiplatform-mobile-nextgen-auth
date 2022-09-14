package kg.optima.mobile.registration.presentation.create_password.validity

data class PasswordValidityModel(
	val isValid : Boolean,
	val criteriaText : String
) {
	companion object {
		val BASIC_VALIDITY = listOf(
			PasswordValidityModel(
				criteriaText = "Заглавные буквы",
				isValid = false
			),
			PasswordValidityModel(
				criteriaText = "Цифры",
				isValid = false
			),
			PasswordValidityModel(
				criteriaText = "Минимум 8 символов",
				isValid = false
			)
		)
	}
}