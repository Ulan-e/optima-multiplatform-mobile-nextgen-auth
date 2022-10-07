package kg.optima.mobile.common.presentation.welcome

sealed interface WelcomeEntity {
	object Login: WelcomeEntity

	object Register : WelcomeEntity
}