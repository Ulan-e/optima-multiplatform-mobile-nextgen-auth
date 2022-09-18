package kg.optima.mobile.design_system.android.ui.dropdown_list


data class DropDownItemsList<T>(
	val list: List<DropDownItemModel<T>>,
	val selectedItemIndex: Int = 0
) {
	val selectedItem
		get() = if (list.isNotEmpty()) list[selectedItemIndex] else null
}

data class DropDownItemModel<T>(
	val title: String,
	val entity: T
)