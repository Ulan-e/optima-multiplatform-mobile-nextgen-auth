package kg.optima.mobile.design_system.android.ui.dropdown_list

data class DropDownItemModel<T>(
	val title : String,
	val entity : T
)

data class DropDownItemsList<T>(
	val list : List<DropDownItemModel<T>>,
	val selectedItemIndex : Int
) {
	val selectedItem = list[selectedItemIndex]

	fun pickItem(item : DropDownItemModel<T>) : DropDownItemsList<T> {
		return this.copy(selectedItemIndex = this.list.indexOf(item))
	}
}