package kg.optima.mobile.design_system.android.ui.dropdown_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.Typography
import kg.optima.mobile.resources.images.MainImages

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <T> DropDownList(
	items: DropDownItemsList<T>,
	expanded: Boolean = false,
	onItemSelected: (T) -> Unit,
	onExpandClick: () -> Unit,
	onDismiss: () -> Unit,
	modifier: Modifier,
	keyboardController: SoftwareKeyboardController?
) {

	var contentWidth by remember { mutableStateOf(Size.Zero) }
	var selected = remember { mutableStateOf(false) }

	val icon = if (expanded) painterResource(id = MainImages.arrowUp.resId())
	else painterResource(id = MainImages.arrowDown.resId())

	Surface(
		elevation = if (expanded) 4.dp else 0.dp,
		shape = RoundedCornerShape(Deps.inputFieldCornerRadius),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = modifier
				.background(ComposeColors.OpaquedDisabledGray)
				.defaultMinSize(
					minWidth = TextFieldDefaults.MinWidth,
					minHeight = TextFieldDefaults.MinHeight
				)
				.onGloballyPositioned { coordinates ->
					contentWidth = coordinates.size.toSize()
				}
				.clickable(
					indication = null,
					interactionSource = remember { MutableInteractionSource() }) {
					keyboardController?.hide()
					onExpandClick.invoke()
				},
		) {
			Text(
				modifier = Modifier
					.weight(1f)
					.padding(start = Deps.Spacing.standardMargin)
					.clickable(
						indication = null,
						interactionSource = remember { MutableInteractionSource() }
					) {
						keyboardController?.hide()
						onExpandClick.invoke()
					},
				text = if (selected.value) {
					items.selectedItem!!.title
				} else {
					"Контрольный вопрос"
				},
				fontSize = Headings.H4.sp,
				fontWeight = FontWeight.W500,
				color = if (!selected.value) {
					ComposeColors.DescriptionGray
				} else {
					ComposeColors.PrimaryDisabledGray
				}
			)
			Icon(
				icon, contentDescription = "icon",
				modifier = Modifier
					.size(Deps.Size.dropDownIconSize)
					.padding(end = Deps.Spacing.standardMargin)
			)

			MaterialTheme(
				colors = MaterialTheme.colors.copy(surface = ComposeColors.WhiteF5),
				shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(Deps.inputFieldCornerRadius))
			) {
				DropdownMenu(
					offset = DpOffset(y = 8.dp, x = 0.dp),
					expanded = expanded,
					onDismissRequest = onDismiss
				) {
					items.list.forEachIndexed { i, selectedOption ->
						if (selectedOption.title != "Контрольный вопрос") {
							DropdownMenuItem(
								modifier = Modifier
									.width(with(LocalDensity.current) { contentWidth.width.toDp() }),
								onClick = {
									onItemSelected(selectedOption.entity)
									selected.value = true
								}
							) {
								Text(
									modifier = Modifier
										.fillMaxWidth()
										.padding(end = Deps.Spacing.standardPadding),
									text = selectedOption.title,
									fontSize = Headings.H4.sp,
									fontWeight = FontWeight.W500,
									color = ComposeColors.PrimaryDisabledGray
								)
							}
							if (i != items.list.lastIndex) {
								Divider(
									thickness = Deps.Spacing.minPadding,
									color = Color(0xFF999BA3),
									modifier = Modifier
										.fillMaxWidth()
										.padding(start = Deps.Spacing.standardMargin)
										.padding(vertical = Deps.Spacing.minPadding * 4),
								)
							}
						}
					}
				}
			}
		}
	}
}