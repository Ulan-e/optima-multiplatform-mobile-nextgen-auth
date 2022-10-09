package kg.optima.mobile.navigation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState

class TransfersComponent(
	override val componentContext: ComponentContext,
) : Root.Child.Component, ComponentContext by componentContext {

	private val stackNavigation = StackNavigation<Config>()

	private val childStack =
		childStack(
			source = stackNavigation,
			initialConfiguration = Config.MainPage(),
			key = "FirstStack",
			childFactory = { config, _ -> childFactory(config) },
		)

	override val backStack: Value<ChildStack<*, UiState.Model.Navigate>> = childStack

	override fun addAll(screenModels: List<UiState.Model.Navigate>) {
		// TODO check if SM is not TSM
		screenModels.forEach {
			stackNavigation.push(it.toConfig())
		}
	}

	override fun pop() = stackNavigation.pop()

	private fun childFactory(
		config: Config,
	): UiState.Model.Navigate = when (config) {
		is Config.MainPage -> TODO()
	}

	private sealed interface Config : Parcelable {
		@Parcelize
		class MainPage : Config
	}

	private fun UiState.Model.Navigate.toConfig(): Config {
		return when (this) {
			else -> Config.MainPage()
		}
	}
}