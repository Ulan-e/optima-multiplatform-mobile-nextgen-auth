package kg.optima.mobile.navigation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.menu.MenuScreenModel

class MenuComponent(
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

	override val backStack: Value<ChildStack<*, ScreenModel>> = childStack

	override fun addAll(screenModels: List<ScreenModel>) {
		// TODO check if SM is not MSM
		screenModels.filterIsInstance<MenuScreenModel>().forEach {
			stackNavigation.push(it.toConfig())
		}
	}

	override fun pop() = stackNavigation.pop()

	private fun childFactory(config: Config) =
		when (config) {
			is Config.MainPage -> MenuScreenModel.Main
		}

	private sealed interface Config : Parcelable {
		@Parcelize
		class MainPage : Config
	}

	private fun MenuScreenModel.toConfig(): Config {
		return when (this) {
			MenuScreenModel.Main -> Config.MainPage()
		}
	}
}
