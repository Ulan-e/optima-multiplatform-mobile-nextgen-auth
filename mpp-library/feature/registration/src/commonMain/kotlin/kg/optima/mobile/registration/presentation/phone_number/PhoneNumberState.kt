package kg.optima.mobile.registration.presentation.phone_number

import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.common.Constants

class PhoneNumberState : UiState<CheckPhoneNumberInfo>() {

	override fun handle(entity: CheckPhoneNumberInfo) {
		val stateModel: Model = when (entity) {
			is CheckPhoneNumberInfo.Validation ->
				PhoneNumberStateModel.ValidateResult(entity.success, entity.message)
			is CheckPhoneNumberInfo.Check -> {
				if (entity.success) {
					PhoneNumberStateModel.NavigateTo.RegistrationAcceptCode(
						phoneNumber = phoneFormatter(entity.phoneNumber),
						timeLeft = entity.timeLeft,
						referenceId = entity.referenceId
					)
				} else {
					Model.Error.BaseError(entity.message)
				}
			}
			CheckPhoneNumberInfo.NavigateToMain ->
				PhoneNumberStateModel.NavigateTo.Welcome
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

	sealed interface PhoneNumberStateModel : Model {
		class ValidateResult(
			val success: Boolean,
			val message: String = emptyString,
		) : PhoneNumberStateModel

		sealed interface NavigateTo : PhoneNumberStateModel, Model.Navigate {
			@Parcelize
			object Welcome : NavigateTo

			@Parcelize
			class RegistrationAcceptCode(
				val phoneNumber: String,
				val timeLeft: Long,
				val referenceId: String,
			) : NavigateTo
		}
	}
}