package kg.optima.mobile.design_system.android.ui.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.common.Constants
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.MainImages
import kg.optima.mobile.resources.resId
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CodeInput(
	modifier: Modifier = Modifier,
	length: Int = Constants.PIN_LENGTH,
	value: String,
	onValueChanged: (String) -> Unit,
) {
	val focusRequester = remember { FocusRequester() }
	val keyboard = LocalSoftwareKeyboardController.current
	TextField(
		modifier = Modifier
			.size(Deps.Size.invisible)
			.focusRequester(focusRequester),
		value = value,
		onValueChange = {
			if (it.length <= length) {
				if (it.all { c -> c in '0'..'9' }) {
					onValueChanged(it)
				}
				if (it.length >= length) {
					keyboard?.hide()
				}
			}
		},
		keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
	)

	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.Center,
	) {
		repeat(length) {
			OtpCell(
				modifier = modifier.clickable {
					focusRequester.requestFocus(); keyboard?.show()
				},
				cellStatus = when {
					value.length == it -> CellStatus.Focused
					value.length > it -> CellStatus.Filled
					else -> CellStatus.Empty
				},
			)
		}
	}
}

@Composable
private fun OtpCell(
	modifier: Modifier = Modifier,
	cellStatus: CellStatus,
) {
	val scope = rememberCoroutineScope()
	val (cursorSymbol, setCursorSymbol) = remember { mutableStateOf(emptyString) }

	LaunchedEffect(key1 = cursorSymbol, cellStatus) {
		if (cellStatus == CellStatus.Focused) {
			scope.launch {
				delay(500)
				setCursorSymbol(if (cursorSymbol.isEmpty()) "|" else emptyString)
			}
		}
	}

	Surface(
		modifier = modifier
			.padding(horizontal = Deps.Spacing.pinCellXMargin)
			.size(
				width = Deps.Size.pinCellSize.first,
				height = Deps.Size.pinCellSize.second
			),
		border = BorderStroke(
			width = Deps.borderStroke,
			color = if (cellStatus == CellStatus.Focused) {
				ComposeColors.PrimaryBlack
			} else {
				Color.Transparent
			},
		),
		shape = RoundedCornerShape(size = Deps.cornerRadius),
	) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(ComposeColors.WhiteF5)
		) {
			when (cellStatus) {
				CellStatus.Empty -> Unit
				CellStatus.Focused -> Text(
					text = cursorSymbol,
					modifier = Modifier.align(Alignment.Center),
					fontSize = Headings.H1.px.sp,
				)
				CellStatus.Filled -> Icon(
					modifier = Modifier
						.size(10.dp)
						.align(Alignment.Center),
					painter = painterResource(id = MainImages.dot.resId()),
					contentDescription = emptyString,
					tint = ComposeColors.PrimaryBlack,
				)
			}
		}
	}
}

private enum class CellStatus {
	Empty, Focused, Filled;
}