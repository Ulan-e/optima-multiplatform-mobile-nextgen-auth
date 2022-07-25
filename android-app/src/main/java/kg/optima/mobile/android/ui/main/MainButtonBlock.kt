package kg.optima.mobile.android.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.design_system.android.values.sp
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.Headings.Companion.px
import kg.optima.mobile.resources.images.MainImages
import kg.optima.mobile.resources.resId

@Composable
fun MainButtonBlock(
    modifier: Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            MainButton(
                imageResId = MainImages.bell.resId(),
                text = "Уведомления",
            ) {

            }
            MainButton(
                imageResId = MainImages.chartup.resId(),
                text = "Курсы валют",
            ) {

            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            MainButton(
                imageResId = MainImages.ellipse.resId(),
                text = "Языки",
            ) {

            }
            MainButton(
                imageResId = MainImages.phone.resId(),
                text = "Контакты",
            ) {

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainButton(
    imageResId: Int,
    text: String,
    onClick: () -> Unit,
) {
    val colors = ButtonDefaults.buttonColors()
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        color = Color.Transparent,
        interactionSource = interactionSource,
    ) {
        Column(
            modifier = Modifier
                .size(width = 100.dp, height = 77.dp)
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(
                        width = Deps.mainButtonSize.first,
                        height = Deps.mainButtonSize.second
                    )
                    .background(
                        color = ComposeColors.primaryRed,
                        shape = RoundedCornerShape(Deps.mainButtonBackgroundRadius)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.size(Deps.mainButtonImageSize),
                    painter = painterResource(id = imageResId),
                    contentDescription = ""
                )
            }
            Text(
                text = text,
                fontSize = Headings.H5.px().sp(),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}