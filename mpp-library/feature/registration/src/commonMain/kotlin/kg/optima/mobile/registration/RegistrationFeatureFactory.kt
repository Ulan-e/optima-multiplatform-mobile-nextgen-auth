package kg.optima.mobile.registration

import kg.optima.mobile.base.di.Factory
import kg.optima.mobile.registration.domain.CheckPhoneNumberUseCase
import kg.optima.mobile.registration.presentation.agreement.AgreementIntent
import kg.optima.mobile.registration.presentation.agreement.AgreementState
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberIntent
import kg.optima.mobile.registration.presentation.phone_number.PhoneNumberState
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.dsl.module

object RegistrationFeatureFactory : Factory(), KoinComponent {

    override val module: Module = module {

        factory { CheckPhoneNumberUseCase() }

        factory { AgreementState() }
        factory { st -> AgreementIntent(st.get()) }

        factory { PhoneNumberState() }
        factory { st -> PhoneNumberIntent(st.get()) }
    }

}