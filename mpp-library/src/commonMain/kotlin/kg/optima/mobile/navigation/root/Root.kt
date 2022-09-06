package kg.optima.mobile.navigation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.navigation.BottomNavItem

interface Root {
	val childStack: Value<ChildStack<*, Child>>

	fun onItemClicked(item: BottomNavItem)

	sealed interface Child {
		val component: Component

		class Main(componentContext: ComponentContext) : Child {
			override val component: MainComponent = MainComponent(componentContext)
		}

		class Transfers(componentContext: ComponentContext) : Child {
			override val component: TransfersComponent = TransfersComponent(componentContext)
		}

		class Payments(componentContext: ComponentContext) : Child {
			override val component: PaymentsComponent = PaymentsComponent(componentContext)
		}

		class History(componentContext: ComponentContext) : Child {
			override val component: HistoryComponent = HistoryComponent(componentContext)
		}

		class Menu(componentContext: ComponentContext) : Child {
			override val component: MenuComponent = MenuComponent(componentContext)
		}

		interface Component {
			val componentContext: ComponentContext
			val backStack: Value<ChildStack<*, ScreenModel>>

			val items get() = backStack.value.items.map { it.configuration }

			fun addAll(screenModels: List<ScreenModel>)
			fun addAll(screenModel: ScreenModel) = addAll(listOf(screenModel))

			fun pop()
		}
	}
}

