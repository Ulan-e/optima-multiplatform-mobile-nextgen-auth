package kg.optima.mobile.design_system.android.ui.buttons.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.SecondaryButton
import kg.optima.mobile.design_system.android.ui.buttons.TransparentButton
import kg.optima.mobile.resources.ComposeColors
import kg.optima.mobile.resources.Headings

sealed interface ButtonView {
	val modifier: Modifier
	val enabled: Boolean
	val text: String
	val fontSize: Headings
	val onClick: () -> Unit

	val button: @Composable () -> Unit

	class Primary(
		override val modifier: Modifier = Modifier,
		override val enabled: Boolean = true,
		override val text: String,
		override val fontSize: Headings = Headings.H4,
		override val onClick: () -> Unit,
		private val color: Color = ComposeColors.PrimaryRed,
	) : ButtonView {
		override val button: @Composable () -> Unit = {
			PrimaryButton(modifier, enabled, text, fontSize, color, onClick)
		}
	}

	class Secondary(
		override val modifier: Modifier = Modifier,
		override val enabled: Boolean = true,
		override val text: String,
		override val fontSize: Headings = Headings.H4,
		override val onClick: () -> Unit,
		private val buttonSecondaryType: ButtonSecondaryType,
	) : ButtonView {
		override val button: @Composable () -> Unit = {
			SecondaryButton(modifier, enabled, text, fontSize, buttonSecondaryType, onClick)
		}
	}

	class Transparent(
		override val modifier: Modifier = Modifier,
		override val enabled: Boolean = true,
		override val text: String,
		override val fontSize: Headings = Headings.H4,
		override val onClick: () -> Unit
	) : ButtonView {
		override val button: @Composable () -> Unit = {
			TransparentButton(modifier, enabled, text, fontSize, onClick)
		}
	}
}