package kg.optima.mobile.design_system.android.ui.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors

@Composable
fun BaseBottomSheet(
	modifier: Modifier = Modifier,
	content: @Composable (Modifier) -> Unit,
) {
	Card(
		modifier = modifier
			.fillMaxWidth()
			.background(color = ComposeColors.WhiteF5),
		shape = RoundedCornerShape(
			topStart = Deps.cardCornerRadius,
			topEnd = Deps.cardCornerRadius,
		),
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = Deps.Spacing.swiperTopMargin),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Box(
				modifier = Modifier
					.size(
						width = Deps.Size.sheetSwiperSize.first,
						height = Deps.Size.sheetSwiperSize.second
					)
					.background(
						color = ComposeColors.OpaquedDisabledGray20,
						shape = RoundedCornerShape(Deps.checkboxCornerRadius)
					),
			)
			content(
				Modifier.padding(
					start = Deps.Spacing.standardPadding,
					top = Deps.Spacing.spacing,
					end = Deps.Spacing.standardPadding,
					bottom = Deps.Spacing.standardMargin,
				)
			)
		}
	}
}