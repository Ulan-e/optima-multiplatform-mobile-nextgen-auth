package kg.optima.mobile.design_system.android.ui.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kg.optima.mobile.common.Constants
import kg.optima.mobile.design_system.android.ui.input.debounce.debounce
import kg.optima.mobile.design_system.android.ui.input.filter.ITextFieldComposeFilter
import kg.optima.mobile.design_system.android.ui.input.filter.NoTextFieldFilter
import kg.optima.mobile.design_system.android.ui.input.model.ErrorTextField
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.Headings.Companion.px
import kg.optima.mobile.resources.sp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputField(
	modifier: Modifier = Modifier,
	valueState: MutableState<String>,
	title: String = "",
	hint: String = "",
	enabled: Boolean = true,
	keyboardType: KeyboardType = KeyboardType.Text,
	imeAction: ImeAction = ImeAction.Done,
	maxLength: Int = Constants.MAX_LENGTH_INPUT,
	singleLine: Boolean = true,
	visualTransformation: VisualTransformation = VisualTransformation.None,
	leadingIcon: @Composable (() -> Unit)? = null,
	trailingIcon: @Composable (() -> Unit)? = null,
	onValueChange: ((String) -> Unit)? = null,
	onDebounceValueChange: ((String) -> Unit)? = null,
	filter: List<ITextFieldComposeFilter> = listOf(NoTextFieldFilter()),
	bottomActionButton: Pair<String, () -> Unit>? = null,
	isFocused: Boolean = false,
	errorState: MutableState<ErrorTextField> = mutableStateOf(ErrorTextField.empty()),
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
	clearErrorTextOnChange: Boolean = true,
) {
	val keyboardController = LocalSoftwareKeyboardController.current
	val focusManager = LocalFocusManager.current

	val (focusRequester) = FocusRequester.createRefs()

	val iconFocusColor = Color.Black
	val iconColor = Color.Black
	val focusedColor = remember { mutableStateOf(iconColor) }

	val scope = rememberCoroutineScope()
	val debounceTextChange = remember {
		debounce<String>(Constants.DEBOUNCE_WAIT_MS, scope) {
			onDebounceValueChange?.invoke(it)
		}
	}

	if (title.isNotBlank()) {
		Text(
			text = title,
			modifier = Modifier
				.fillMaxWidth()
				.absolutePadding(bottom = 4.dp),
			color = Color.Black,
		)
	}

	TextField(
		modifier = modifier
			.fillMaxWidth()
			.focusRequester(focusRequester),
		value = valueState.value,
		isError = errorState.value.isError,
		onValueChange = {
			if (onValueChange != null) {
				onValueChange.invoke(it)
			} else if (it.length <= maxLength) {
				valueState.value = it
			}

			if (onDebounceValueChange != null) {
				debounceTextChange(it)
			}
			if (clearErrorTextOnChange) {
				errorState.value = ErrorTextField.empty()
			}
		},
		placeholder = {
			Text(
				text = hint,
				color = ComposeColors.secondaryDescriptionGray,
			)
		},
		singleLine = singleLine,
		enabled = enabled,
		keyboardOptions = KeyboardOptions(
			keyboardType = keyboardType,
			imeAction = imeAction
		),
		keyboardActions = KeyboardActions(
			onDone = { keyboardController?.hide() },
			onNext = { focusManager.moveFocus(FocusDirection.Down) }),
		visualTransformation = visualTransformation,
		leadingIcon = leadingIcon,
		trailingIcon = trailingIcon,
		shape = RoundedCornerShape(Deps.inputFieldCornerRadius),
		colors = TextFieldDefaults.textFieldColors(
			backgroundColor = ComposeColors.opaquedDisabledGray,
			cursorColor = Color.Black,
			focusedIndicatorColor = Color.Transparent,
			disabledIndicatorColor = Color.Transparent,
			unfocusedIndicatorColor = Color.Transparent,
			leadingIconColor = focusedColor.value,
			trailingIconColor = focusedColor.value
		),
		interactionSource = interactionSource,
	)

	if (bottomActionButton != null) {
		Text(
			modifier = Modifier
				.clickable(onClick = bottomActionButton.second)
				.padding(top = Deps.marginFromInput),
			text = bottomActionButton.first,
			color = ComposeColors.secondaryGreen,
			fontSize = Headings.H5.px().sp()
		)
	}

	if (errorState.value.message.isNotEmpty()) {
		Text(
			modifier = Modifier.padding(top = Deps.marginFromInput),
			text = errorState.value.message,
			color = ComposeColors.primaryRed,
		)
	}

	//todo add later
	if (isFocused) {
		LaunchedEffect(Unit) {
			focusRequester.requestFocus()
		}
	}
}