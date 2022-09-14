package kg.optima.mobile.navigation

import dev.icerock.moko.resources.ImageResource
import kg.optima.mobile.resources.images.MainImages


enum class BottomNavItem(val res: ImageResource?, val title: String) {
	Main(MainImages.main, "Главная"),
	Transfers(MainImages.transfers, "Переводы"),
	Payments(MainImages.payments, "Платежи"),
	History(MainImages.history, "История"),
	Menu(MainImages.menu, "Меню");
}

val navItemList = listOf(
	BottomNavItem.Main,
	BottomNavItem.Transfers,
	BottomNavItem.Payments,
	BottomNavItem.History,
	BottomNavItem.Menu,
)
