package kg.optima.mobile.design_system.android.ui.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings

@Composable
fun TransparentButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    fontSize: Headings = Headings.H4,
    onClick: () -> Unit = {},
) = Button(
    modifier = modifier.height(Deps.buttonHeight),
    shape = MaterialTheme.shapes.small,
    colors = ButtonDefaults.buttonColors(
        backgroundColor = Color.Transparent,
        disabledBackgroundColor = Color.Transparent,
        disabledContentColor = ComposeColors.primaryDisabledGray,
    ),
    elevation = null,
    enabled = enabled,
    onClick = onClick,
) {
    Text(
        text = text,
        fontSize = fontSize.px.sp,
        color = ComposeColors.primaryRed,
    )
}