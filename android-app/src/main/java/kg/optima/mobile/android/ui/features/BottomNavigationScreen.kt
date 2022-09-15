package kg.optima.mobile.android.ui.features

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.ui.features.history.HistoryContent
import kg.optima.mobile.android.ui.features.main.MainContent
import kg.optima.mobile.android.ui.features.menu.MenuContent
import kg.optima.mobile.android.ui.features.payments.PaymentsContent
import kg.optima.mobile.android.ui.features.transfers.TransfersContent
import kg.optima.mobile.android.utils.asActivity
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.navigation.BottomNavItem
import kg.optima.mobile.navigation.navItemList
import kg.optima.mobile.navigation.root.Root
import kg.optima.mobile.navigation.root.RootComponent
import kg.optima.mobile.resources.Headings

@Parcelize
object BottomNavigationScreen : BaseScreen {

	@OptIn(ExperimentalDecomposeApi::class)
	@Composable
	override fun Content() {
		val context = LocalContext.current
		val root = RootComponent(context.asActivity()!!.defaultComponentContext())
		val childStack by root.childStack.subscribeAsState()
		val activeComponent = childStack.active.instance

		val navigator = LocalNavigator.currentOrThrow
		if (!navigator.canPop) {
			val activity = LocalContext.current.asActivity()
			BackHandler { activity?.finish() }
		}

		Scaffold(
			bottomBar = {
				BottomNavigation(
					backgroundColor = ComposeColors.PrimaryWhite,
				) {
					navItemList.forEach {
						val selected = activeComponent.isInstance(it)
						BottomNavigationItem(
							selected = selected,
							onClick = { root.onItemClicked(it) },
							selectedContentColor = ComposeColors.PrimaryRed,
							unselectedContentColor = ComposeColors.DescriptionGray,
							icon = {
								Column(horizontalAlignment = Alignment.CenterHorizontally) {
									Icon(
										modifier = Modifier
											.size(Deps.Size.mainButtonImageSize)
											.padding(bottom = Deps.Spacing.iconDescriptionMargin),
										painter = painterResource(id = it.res.resId()),
										contentDescription = it.title,
									)
									Text(
										text = it.title,
										fontSize = Headings.H7.sp,
									)
								}
							}
						)
					}
				}
			},
			content = { all ->
				Children(
					stack = childStack,
					modifier = Modifier.padding(all)
				) {
					when (val child = it.instance) {
						is Root.Child.Main -> MainContent(child.component)
						is Root.Child.Transfers -> TransfersContent(child.component)
						is Root.Child.Payments -> PaymentsContent(child.component)
						is Root.Child.History -> HistoryContent(child.component)
						is Root.Child.Menu -> MenuContent(child.component)
					}
				}
			}
		)
	}
}

private fun Root.Child.isInstance(item: BottomNavItem) = when (this) {
	is Root.Child.History -> item == BottomNavItem.History
	is Root.Child.Main -> item == BottomNavItem.Main
	is Root.Child.Menu -> item == BottomNavItem.Menu
	is Root.Child.Payments -> item == BottomNavItem.Payments
	is Root.Child.Transfers -> item == BottomNavItem.Transfers
}