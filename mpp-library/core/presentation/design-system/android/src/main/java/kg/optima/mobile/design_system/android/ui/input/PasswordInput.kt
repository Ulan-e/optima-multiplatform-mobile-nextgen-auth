package kg.optima.mobile.design_system.android.ui.input

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import kg.optima.mobile.design_system.android.ui.input.model.ErrorTextField
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.resources.images.MainImages
import kg.optima.mobile.design_system.android.utils.resources.resId


@SuppressLint("ResourceType")
@Composable
fun PasswordInput(
	modifier: Modifier = Modifier,
	passwordState: MutableState<String>,
	hint: String = "",
	title: String = "",
	enabled: Boolean = true,
	imeAction: ImeAction = ImeAction.Next,
	onFocusLost: () -> Unit = {},
	isFocused: Boolean = false,
	errorState: MutableState<ErrorTextField> = mutableStateOf(ErrorTextField.empty()),
	onValueChange: ((String) -> Unit) = {},
	onDebounceValueChange: ((String) -> Unit)? = null,
) {
	val visualTransformation = remember {
		mutableStateOf<VisualTransformation>(PasswordVisualTransformation())
	}

	InputField(
		valueState = passwordState,
		isFocused = isFocused,
		errorState = errorState,
		onValueChange = {
			errorState.value = ErrorTextField.empty()
			passwordState.value = it
			onValueChange(it)
		},
		title = title,
		hint = hint,
		modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (!it.hasFocus) onFocusLost()
            },
		enabled = enabled,
		keyboardType = KeyboardType.Password,
		imeAction = imeAction,
		visualTransformation = visualTransformation.value,
		trailingIcon = {
			PasswordVisibility(
				onClick = { isHidden: Boolean ->
					visualTransformation.value = if (isHidden) {
						VisualTransformation.None
					} else {
						PasswordVisualTransformation()
					}
				},
			)
		},
		onDebounceValueChange = onDebounceValueChange,
	)
}

@Composable
private fun PasswordVisibility(onClick: (Boolean) -> Unit) {
	val visible = remember { mutableStateOf(false) }
	val icon = painterResource(
		id = (if (visible.value) MainImages.invisible else MainImages.visible).resId()
	)

	IconButton(
		onClick = {
			visible.value = !visible.value
			onClick(visible.value)
		},
		content = {
			Icon(
				modifier = Modifier.size(Deps.Size.trailingIconSize),
				painter = icon,
                contentDescription = "",
                tint = ComposeColors.DescriptionGray,
			)
		}
	)
}