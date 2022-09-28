package kg.optima.mobile.design_system.android.ui.text_fields

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.values.Deps

@Composable
fun PhoneNumberTextField(
    modifier: Modifier,
    phoneNumber: String,
    singleLine: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(Deps.Spacing.standardMargin),
    onValueChange: (String) -> Unit,
    backgroundColor: Color = ComposeColors.PrimaryLightGray,
) {
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier,
        value = phoneNumber,
        onValueChange = { phone ->
            if (phone.length <= Constants.PHONE_NUMBER_LENGTH)
                onValueChange.invoke(phone.filter { it.isDigit() })
        },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        visualTransformation = { text -> maskFilter(text) },
        shape = shape,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = backgroundColor,
            focusedIndicatorColor = ComposeColors.PrimaryWhite,
            unfocusedIndicatorColor = ComposeColors.PrimaryWhite,
        ),
    )
}

private fun maskFilter(text: AnnotatedString): TransformedText {
    val trimmed = if (text.text.length >= Constants.PHONE_NUMBER_LENGTH) {
        text.text.substring(0 until Constants.PHONE_NUMBER_LENGTH)
    } else {
        text.text
    }
    val annotatedString = AnnotatedString.Builder().run {
        append(Constants.PHONE_NUMBER_CODE)
        for (i in trimmed.indices) {
            if (i == 0) append("(")
            append(trimmed[i])

            if (i == 2) append(")")
            if (i % 3 == 2 && i != 8) append(" ")
        }
        pushStyle(style = SpanStyle(color = Color.LightGray))
        val mask = Constants.PHONE_NUMBER_MASK
        append(mask.takeLast(mask.length - length))

        return@run this.toAnnotatedString()
    }

    val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return when {
                offset <= 3 -> offset + 6
                offset <= 6 -> offset + 8
                offset <= 9 -> offset + 9
                else -> 18
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 5) return 0
            if (offset <= 9) return offset - 6
            if (offset <= 14) return offset - 8
            if (offset <= 18) return offset - 9
            return 9
        }
    }

    return TransformedText(annotatedString, creditCardOffsetTranslator)
}