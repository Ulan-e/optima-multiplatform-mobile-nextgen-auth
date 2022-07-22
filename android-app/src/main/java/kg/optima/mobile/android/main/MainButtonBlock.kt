package kg.optima.mobile.android.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.design_system.android.values.sp
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.Headings.Companion.px
import kg.optima.mobile.resources.Images
import kg.optima.mobile.resources.resId

@Composable
fun MainButtonBlock() {
	Row {
		Column() {
			MainButton(
				imageResId = Images.byFileName("img_bell").resId(0),
				text = "Уведомления",
			)
			MainButton(
				imageResId = Images.byFileName("img_chartup").resId(0),
				text = "Курсы валют",
			)
		}
		Column() {
			MainButton(
				imageResId = Images.byFileName("img_ellipse").resId(0),
				text = "Языки",
			)
			MainButton(
				imageResId = Images.byFileName("img_phone").resId(0),
				text = "Контакты",
			)
		}
	}
}

@Composable
fun MainButton(imageResId: Int, text: String) {
	Column(horizontalAlignment = Alignment.CenterHorizontally) {
		Box(
			modifier = Modifier
				.size(width = Deps.mainButtonSize.first, height = Deps.mainButtonSize.second)
				.background(
					color = ComposeColors.primaryRed,
					shape = RoundedCornerShape(Deps.mainButtonBackgroundRadius)
				),
			contentAlignment = Alignment.Center
		) {
			Image(
				painter = painterResource(id = imageResId),
				contentDescription = ""
			)
		}
		Text(
			modifier = Modifier.offset(y = Deps.standardPadding),
			text = text,
			fontSize = Headings.H5.px().sp()
		)
	}
}