package kg.optima.mobile.design_system.android.ui.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings

@Composable
fun BottomSheet(
	bottomSheetInfo: BottomSheetInfo,
	modifier: Modifier = Modifier,
) {
	BaseBottomSheet(modifier) {
		Column(
			modifier = it,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Text(
				text = bottomSheetInfo.title,
				color = ComposeColors.PrimaryBlack,
				fontSize = Headings.H1.px.sp,
				fontWeight = FontWeight.Bold,
				textAlign = TextAlign.Center,
			)
			if (bottomSheetInfo.description != null) {
				Text(
					modifier = Modifier.padding(vertical = Deps.Spacing.subheaderMargin),
					text = bottomSheetInfo.description,
					color = ComposeColors.PrimaryBlack,
					fontSize = Headings.H4.px.sp,
				)
			}
			bottomSheetInfo.content()
			Spacer(modifier = Modifier.padding(top = Deps.Spacing.spacing))
			bottomSheetInfo.buttons.forEach { view -> view.button() }
		}
	}
}