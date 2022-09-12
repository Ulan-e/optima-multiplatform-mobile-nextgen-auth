package kg.optima.mobile.registration.presentation.phone_number

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.feature.register.RegistrationScreenModel

class PhoneNumberState : State<CheckPhoneNumberInfo>() {

	override fun handle(entity: CheckPhoneNumberInfo) {
		val stateModel: StateModel = when (entity) {
			is CheckPhoneNumberInfo.Validation ->
				PhoneNumberStateModel.ValidateResult(entity.success, entity.message)
			is CheckPhoneNumberInfo.Check -> {
				if (entity.success) {
					val screenModel = RegistrationScreenModel.AcceptCode(entity.phoneNumber)
					StateModel.Navigate(screenModel)
				} else {
					StateModel.Error.BaseError("Неверный номер телефона!")
				}
			}
		}

		setStateModel(stateModel)
	}

	sealed interface PhoneNumberStateModel : StateModel {
		class ValidateResult(
			val success: Boolean,
			val message: String = emptyString,
		) : PhoneNumberStateModel
	}
}