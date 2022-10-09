package kg.optima.mobile.android.ui.features.welcome

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
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.MainImages

@Composable
fun WelcomeScreenButtonBlock(
	modifier: Modifier,
	verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
	onMapClick: () -> Unit = {},
	onLangClick: () -> Unit = {},
	onRatesClick: () -> Unit = {},
	onContactsClick: () -> Unit = {},
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(64.dp),
		verticalAlignment = verticalAlignment,
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(40.dp)
		) {
			Button(
				imageResId = MainImages.pin.resId(),
				text = "На карте",
				onClick = onMapClick
			)
			Button(
				imageResId = MainImages.chartup.resId(),
				text = "Курсы валют",
				onClick = onRatesClick
			)
		}
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(40.dp)
		) {
			Button(
				imageResId = MainImages.ellipse.resId(),
				text = "Языки",
				onClick = onLangClick
			)
			Button(
				imageResId = MainImages.phone.resId(),
				text = "Контакты",
				onClick = onContactsClick
			)
		}
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Button(
	imageResId: Int,
	text: String,
	onClick: () -> Unit,
) {
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
						width = Deps.Size.mainButtonSize.first,
						height = Deps.Size.mainButtonSize.second
					)
					.background(
						color = ComposeColors.PrimaryRed,
						shape = RoundedCornerShape(Deps.mainButtonCornerRadius)
					),
				contentAlignment = Alignment.Center
			) {
				Icon(
					modifier = Modifier.size(Deps.Size.mainButtonImageSize),
					painter = painterResource(id = imageResId),
					contentDescription = emptyString,
					tint = ComposeColors.Background,
				)
			}
			Text(
				text = text,
				fontSize = Headings.H5.sp,
				fontWeight = FontWeight.Medium,
			)
		}
	}
}