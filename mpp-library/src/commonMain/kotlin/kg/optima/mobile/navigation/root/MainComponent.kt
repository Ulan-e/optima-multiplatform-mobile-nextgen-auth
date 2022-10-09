package kg.optima.mobile.navigation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.main.presentation.MainNavigationModel

class MainComponent(
	override val componentContext: ComponentContext,
) : Root.Child.Component, ComponentContext by componentContext {

	private val stackNavigation = StackNavigation<Config>()

	private val childStack =
		childStack(
			source = stackNavigation,
			initialConfiguration = Config.MainPage,
			key = "FirstStack",
			childFactory = { config, _ -> childFactory(config) },
		)

	override val backStack: Value<ChildStack<*, UiState.Model.Navigate>> = childStack

	override fun addAll(stateModels: List<UiState.Model.Navigate>) {
		stateModels.filterIsInstance<MainNavigationModel>().forEach {
			stackNavigation.push(it.toConfig())
		}
	}

	override fun pop() = stackNavigation.pop()

	private fun childFactory(
		config: Config,
	): MainNavigationModel = when (config) {
		is Config.MainPage -> MainModel
	}

	private sealed interface Config : Parcelable {
		@Parcelize
		object MainPage : Config
	}

	private fun MainNavigationModel.toConfig(): Config {
		return when (this) {
			else -> Config.MainPage
		}
	}

	@Parcelize
	private object MainModel : MainNavigationModel {

	}
}