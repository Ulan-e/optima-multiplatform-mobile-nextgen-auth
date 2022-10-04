package kg.optima.mobile.registration

import kg.optima.mobile.base.di.Factory
import kg.optima.mobile.registration.data.api.registration.RegistrationApi
import kg.optima.mobile.registration.data.api.registration.RegistrationApiImpl
import kg.optima.mobile.registration.data.api.verification.VerificationApi
import kg.optima.mobile.registration.data.api.verification.VerificationApiImpl
import kg.optima.mobile.registration.data.component.RegistrationPreferences
import kg.optima.mobile.registration.data.component.RegistrationPreferencesImpl
import kg.optima.mobile.registration.data.repository.RegistrationRepository
import kg.optima.mobile.registration.data.repository.RegistrationRepositoryImpl
import kg.optima.mobile.registration.domain.usecase.*
import kg.optima.mobile.registration.presentation.agreement.AgreementIntent
import kg.optima.mobile.registration.presentation.agreement.AgreementState
import kg.optima.mobile.registration.presentation.control_question.ControlQuestionIntent
import kg.optima.mobile.registration.presentation.control_question.ControlQuestionState
import kg.optima.mobile.registration.presentation.create_password.CreatePasswordIntent
import kg.optima.mobile.registration.presentation.create_password.CreatePasswordState
import kg.optima.mobile.registration.presentation.interview.InterviewIntent
import kg.optima.mobile.registration.presentation.interview.InterviewState
import kg.optima.mobile.registration.presentation.liveness.LivenessIntent
import kg.optima.mobile.registration.presentation.liveness.LivenessState
import kg.optima.mobile.registration.presentation.offer.OfferIntent
import kg.optima.mobile.registration.presentation.offer.OfferState
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberIntent
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberState
import kg.optima.mobile.registration.presentation.self_confirm.SelfConfirmIntent
import kg.optima.mobile.registration.presentation.self_confirm.SelfConfirmState
import kg.optima.mobile.registration.presentation.sms_code.SmsCodeIntent
import kg.optima.mobile.registration.presentation.sms_code.SmsCodeState
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.dsl.module

object RegistrationFeatureFactory : Factory {

	override val module: Module = module {
		factory<RegistrationApi> { RegistrationApiImpl(get()) }
		factory<VerificationApi> { VerificationApiImpl(get()) }
		factory<RegistrationRepository> { RegistrationRepositoryImpl(get(), get()) }
		factory<RegistrationPreferences> { RegistrationPreferencesImpl(get()) }

		factory { CheckPhoneNumberUseCase(get(), get()) }
		factory { CheckSmsCodeUseCase(get(), get()) }
		factory { VerifyClientUseCase(get(), get()) }
		factory { GetQuestionsUseCase(get()) }
		factory { RegistrationUseCase(get(), get()) }

		factory { AgreementState() }
		factory { st -> AgreementIntent(st.get()) }

		factory { SmsCodeState() }
		factory { st -> SmsCodeIntent(st.get()) }

		factory { OfferState() }
		factory { st -> OfferIntent(st.get()) }

		factory { PhoneNumberState() }
		factory { st -> PhoneNumberIntent(st.get()) }

		factory { SelfConfirmState() }
		factory { st -> SelfConfirmIntent(st.get()) }

		factory { ControlQuestionState() }
		factory { st -> ControlQuestionIntent(st.get()) }

		factory { CreatePasswordState() }
		factory { st -> CreatePasswordIntent(st.get()) }

		factory { LivenessState() }
		factory { st -> LivenessIntent(st.get()) }

		factory { InterviewState() }
		factory { st -> InterviewIntent(st.get()) }
	}
}