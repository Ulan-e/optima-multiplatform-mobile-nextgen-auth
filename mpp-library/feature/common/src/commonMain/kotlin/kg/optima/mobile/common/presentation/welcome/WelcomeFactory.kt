package kg.optima.mobile.common.presentation.welcome

import kg.optima.mobile.base.di.Factory
import kg.optima.mobile.base.di.ISCreator
import kg.optima.mobile.base.di.create

object WelcomeFactory : ISCreator {
	fun create(): Factory.Product<WelcomeIntent, WelcomeState> = create<WelcomeIntent, WelcomeState>()
}