package kg.optima.mobile.registration.presentation.phone_number

import kg.optima.mobile.base.presentation.State
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.feature.registration.RegistrationScreenModel

class PhoneNumberState : State<CheckPhoneNumberInfo>() {

	override fun handle(entity: CheckPhoneNumberInfo) {
		val stateModel: StateModel = when (entity) {
			is CheckPhoneNumberInfo.Validation ->
				PhoneNumberStateModel.ValidateResult(entity.success, entity.message)
			is CheckPhoneNumberInfo.PhoneNumber -> {
				val screenModel = RegistrationScreenModel.AcceptCode(
					phoneNumber = entity.phoneNumber,
					timeout = 60,
					referenceId = "",
				)
				StateModel.Navigate(screenModel)
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