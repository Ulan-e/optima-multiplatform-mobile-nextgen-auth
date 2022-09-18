package kg.optima.mobile.design_system.android.ui.password_validity

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kg.optima.mobile.registration.presentation.create_password.validity.PasswordValidityModel
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.MainImages

@Composable
fun PasswordValidityItem(model: PasswordValidityModel) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier.fillMaxWidth().padding(bottom = Deps.Spacing.spacing / 2)
	) {
		Icon(
			modifier = Modifier.size(Deps.Size.passValidityDotSize),
			painter = painterResource(id = MainImages.dot.resId()),
			contentDescription = "Indicator",
			tint = if (model.isValid) {ComposeColors.Green} else {ComposeColors.PrimaryRed}
		)
		Text(
			text = model.criteriaText,
			fontSize = Headings.H5.sp,
			color = ComposeColors.DescriptionGray,
			modifier = Modifier.padding(start = Deps.Spacing.marginFromInput)
		)
	}


}