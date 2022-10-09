package kg.optima.mobile.registration.presentation.phone_number

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.registration.presentation.RegistrationNavigateModel

class PhoneNumberState : UiState<CheckPhoneNumberInfo>() {

	override fun handle(entity: CheckPhoneNumberInfo) {
		val stateModel: UiState.Model = when (entity) {
			is CheckPhoneNumberInfo.Validation ->
				Model.ValidateResult(entity.success, entity.message)
			is CheckPhoneNumberInfo.Check -> {
				if (entity.success) {
					Model.NavigateTo.RegistrationSmsCode(
						phoneNumber = phoneFormatter(entity.phoneNumber),
						timeLeft = entity.timeLeft,
						referenceId = entity.referenceId
					)
				} else {
					UiState.Model.Error.BaseError(entity.message)
				}
			}
			CheckPhoneNumberInfo.NavigateToMain ->
				Model.NavigateTo.Welcome
		}

		setStateModel(stateModel)
	}

	private fun phoneFormatter(phone: String): String {
		val builder = StringBuilder().apply {
			append(Constants.PHONE_NUMBER_CODE)
			for (i in phone.indices) {
				if (i == 0) append("(")
				append(phone[i])

				if (i == 2) append(") ")
				if (i % 2 == 0 && i != 8 && i > 2) append(" ")
			}
		}
		return builder.toString()
	}

	sealed interface Model : UiState.Model {
		class ValidateResult(
			val success: Boolean,
			val message: String = emptyString,
		) : Model

		sealed interface NavigateTo : Model, RegistrationNavigateModel {
			@Parcelize
			object Welcome : NavigateTo

			@Parcelize
			class RegistrationSmsCode(
				val phoneNumber: String,
				val timeLeft: Long,
				val referenceId: String,
			) : NavigateTo
		}
	}
}