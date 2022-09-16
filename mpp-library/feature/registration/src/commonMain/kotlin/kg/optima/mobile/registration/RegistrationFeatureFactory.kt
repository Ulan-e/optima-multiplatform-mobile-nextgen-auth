package kg.optima.mobile.registration

import kg.optima.mobile.base.di.Factory
import kg.optima.mobile.registration.data.api.RegistrationApi
import kg.optima.mobile.registration.data.api.RegistrationApiImpl
import kg.optima.mobile.registration.data.component.RegistrationPreferences
import kg.optima.mobile.registration.data.component.RegistrationPreferencesImpl
import kg.optima.mobile.registration.data.repository.RegistrationRepository
import kg.optima.mobile.registration.data.repository.RegistrationRepositoryImpl
import kg.optima.mobile.registration.domain.usecase.CheckPhoneNumberUseCase
import kg.optima.mobile.registration.domain.usecase.CheckSmsCodeUseCase
import kg.optima.mobile.registration.presentation.agreement.AgreementIntent
import kg.optima.mobile.registration.presentation.agreement.AgreementState
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberIntent
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberState
import kg.optima.mobile.registration.presentation.self_confirm.SelfConfirmIntent
import kg.optima.mobile.registration.presentation.self_confirm.SelfConfirmState
import kg.optima.mobile.registration.presentation.sms_code.SmsCodeIntent
import kg.optima.mobile.registration.presentation.sms_code.SmsCodeState
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.dsl.module

object RegistrationFeatureFactory : Factory(), KoinComponent {

    override val module: Module = module {
        factory<RegistrationApi> { RegistrationApiImpl(get()) }
        factory<RegistrationRepository> { RegistrationRepositoryImpl(get()) }
        factory<RegistrationPreferences> { RegistrationPreferencesImpl(get()) }

        factory {
            CheckPhoneNumberUseCase(
                registrationRepository = get(),
                registrationPreferences = get()
            )
        }
        factory {
            CheckSmsCodeUseCase(
                registrationRepository = get(),
                registrationPreferences = get()
            )
        }

        factory { SmsCodeState() }
        factory { st -> SmsCodeIntent(st.get()) }

        factory { AgreementState() }
        factory { st -> AgreementIntent(st.get()) }

        factory { PhoneNumberState() }
        factory { st -> PhoneNumberIntent(st.get()) }

        factory { SelfConfirmState() }
        factory { st -> SelfConfirmIntent(st.get()) }
    }

}