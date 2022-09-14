package kg.optima.mobile.design_system.android.ui.dropdown_list

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.images.MainImages

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <T>DropDownList(
    items: DropDownItemsList<T>,
    expanded : Boolean = false,
    onItemSelected : (DropDownItemsList<T>) -> Unit,
    onExpandClick : () -> Unit,
    onDismiss : () -> Unit,
    modifier: Modifier,
    keyboardController: SoftwareKeyboardController?
) {

    var contentWidth by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded) painterResource(id = MainImages.arrowUp.resId())
    else painterResource(id = MainImages.arrowDown.resId())

    Surface(
        elevation = if (expanded) 4.dp else 0.dp,
        shape = RoundedCornerShape(Deps.inputFieldCornerRadius)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .background(ComposeColors.OpaquedDisabledGray)
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    contentWidth = coordinates.size.toSize()
                },
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeightIn(min = Deps.Size.buttonHeight)
                    .align(Alignment.Center)
                    .padding(Deps.Spacing.pinCellXMargin),
                text = items.selectedItem.title,
            )

            Icon(icon, contentDescription = "icon", modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(Deps.Spacing.marginFromInput)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    keyboardController?.hide()
                    onExpandClick.invoke()
                })

            MaterialTheme(
                colors = MaterialTheme.colors.copy(surface = ComposeColors.WhiteF5),
                shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(Deps.inputFieldCornerRadius))
            ) {
                DropdownMenu(
                    offset = DpOffset(y = 8.dp, x = 0.dp),
                    expanded = expanded,
                    onDismissRequest = onDismiss
                ) {
                    items.list.forEach { selectedOption ->
                        DropdownMenuItem(
                            modifier = Modifier
                                .width(with(LocalDensity.current) { contentWidth.width.toDp() }),
                            onClick = {
                                onItemSelected(items.pickItem(selectedOption))
                            }
                        ) {
                            Text(text = selectedOption.title, color = Color(0xFF404040))
                        }
                        Divider(
                            thickness = Deps.Spacing.minPadding,
                            color = Color(0xFFF1F1F2),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = Deps.Spacing.standardPadding),
                        )
                    }
                }
            }
        }
    }

}
