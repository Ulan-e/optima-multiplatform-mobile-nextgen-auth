package kg.optima.mobile.design_system.android.ui.screens.pin.headers

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import kg.optima.mobile.base.utils.emptyString
import kg.optima.mobile.design_system.android.R
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings
import kg.optima.mobile.resources.images.MainImages

@Composable
fun enterPinScreenHeader(
    onCloseClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    username: String,
//	profileImageId: Painter,
): @Composable ColumnScope.(Modifier) -> Unit = {
    Box(
        modifier = Modifier
			.fillMaxWidth()
			.height(Deps.Size.buttonHeight)
			.padding(horizontal = Deps.Spacing.standardPadding),
        content = {
            Icon(
                modifier = Modifier
					.size(Deps.Size.mainButtonImageSize)
					.align(Alignment.CenterStart)
					.clickable(onClick = onCloseClick),
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = emptyString,
            )
        }
    )
    Row(
        modifier = it
			.fillMaxWidth()
			.padding(horizontal = Deps.Spacing.standardPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        //	CoilImage() TODO later
        Image(
            painter = painterResource(id = R.drawable.ic_avatar_placeholder),
            contentDescription = null
        )
        Text(
            modifier = Modifier
				.weight(1f)
				.padding(horizontal = Deps.Spacing.rowElementMargin),
            text = "$username,\nдобрый день!",
            fontSize = Headings.H3.sp,
            fontWeight = FontWeight.Medium,
        )
        Icon(
            modifier = Modifier
				.size(Deps.Size.mainButtonImageSize)
				.clickable(onClick = onLogoutClick),
            painter = painterResource(id = MainImages.logout.resId()),
            contentDescription = null,
        )
    }

}
