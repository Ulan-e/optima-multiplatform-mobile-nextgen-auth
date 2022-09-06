package kg.optima.mobile.navigation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.navigation.BottomNavItem

class RootComponent constructor(
	componentContext: ComponentContext,
) : Root, ComponentContext by componentContext {

	private val navigation = StackNavigation<Config>()

	private val stack =
		childStack(
			source = navigation,
			initialStack = { listOf(Config.Main) },
			childFactory = ::child,
		)

	override val childStack: Value<ChildStack<*, Root.Child>> = stack

	private fun child(config: Config, componentContext: ComponentContext): Root.Child =
		when (config) {
			Config.Main -> Root.Child.Main(componentContext)
			Config.Transfers -> Root.Child.Transfers(componentContext)
			Config.Payments -> Root.Child.Payments(componentContext)
			Config.History -> Root.Child.History(componentContext)
			Config.Menu -> Root.Child.Menu(componentContext)
		}

	override fun onItemClicked(item: BottomNavItem) {
		val config = when (item) {
			BottomNavItem.Main -> Config.Main
			BottomNavItem.Transfers -> Config.Transfers
			BottomNavItem.Payments -> Config.Payments
			BottomNavItem.History -> Config.History
			BottomNavItem.Menu -> Config.Menu
		}
		navigation.bringToFront(config)
	}

	private sealed interface Config : Parcelable {
		@Parcelize
		object Main : Config

		@Parcelize
		object Transfers : Config

		@Parcelize
		object Payments : Config

		@Parcelize
		object History : Config

		@Parcelize
		object Menu : Config
	}
}
