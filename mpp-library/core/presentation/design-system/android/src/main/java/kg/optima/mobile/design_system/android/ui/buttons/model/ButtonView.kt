package kg.optima.mobile.design_system.android.ui.buttons.model

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.SecondaryButton
import kg.optima.mobile.design_system.android.ui.buttons.TransparentButton
import kg.optima.mobile.design_system.android.utils.resources.ComposeColor
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.resources.Headings
import kotlinx.parcelize.IgnoredOnParcel

sealed interface ButtonView : Parcelable {
	val modifierParameters: ModifierParameters
	val enabled: Boolean
	val text: String
	val fontSize: Headings
	val onClickListener: OnClickListener

	val button: @Composable () -> Unit

	@Parcelize
	class Primary(
		override val modifierParameters: ModifierParameters = ModifierParameters.Default,
		override val enabled: Boolean = true,
		override val text: String,
		override val fontSize: Headings = Headings.H4,
		override val onClickListener: OnClickListener,
		private val composeColor: ComposeColor = ComposeColor.composeColor(ComposeColors.PrimaryRed),
	) : ButtonView {
		@IgnoredOnParcel
		override val button: @Composable () -> Unit = {
			PrimaryButton(modifierParameters.modifier, enabled, text, fontSize, composeColor.colorParameter.color, onClickListener.onClick)
		}
	}

	@Parcelize
	class Secondary(
		override val modifierParameters: ModifierParameters = ModifierParameters.Default,
		override val enabled: Boolean = true,
		override val text: String,
		override val fontSize: Headings = Headings.H4,
		override val onClickListener: OnClickListener,
		private val buttonSecondaryType: ButtonSecondaryType,
	) : ButtonView {
		@IgnoredOnParcel
		override val button: @Composable () -> Unit = {
			SecondaryButton(
				modifier = modifierParameters.modifier,
				enabled = enabled,
				text = text,
				fontSize = fontSize,
				buttonType = buttonSecondaryType,
				onClick = onClickListener.onClick
			)
		}
	}

	@Parcelize
	class Transparent(
		override val modifierParameters: ModifierParameters = ModifierParameters.Default,
		override val enabled: Boolean = true,
		override val text: String,
		override val fontSize: Headings = Headings.H4,
		override val onClickListener: OnClickListener
	) : ButtonView {
		@IgnoredOnParcel
		override val button: @Composable () -> Unit = {
			TransparentButton(modifierParameters.modifier, enabled, text, fontSize, onClickListener.onClick)
		}
	}

	interface ModifierParameters : Parcelable {
		companion object {
			val Default: ModifierParameters = modifierParameters(true)

			fun modifierParameters(fillMaxWidth: Boolean): ModifierParameters = object : ModifierParameters {
				override val fillMaxWidth: Boolean = fillMaxWidth
				override fun describeContents(): Int = 0
				override fun writeToParcel(p0: Parcel?, p1: Int) = Unit
			}
		}
		val fillMaxWidth: Boolean get() = true
		val modifier: Modifier
			get() {
				val modifier = Modifier
				if (fillMaxWidth) modifier.fillMaxWidth()

				return modifier
			}
	}

	interface OnClickListener : Parcelable {
		companion object {
			fun onClickListener(onClick: () -> Unit): OnClickListener {
				return object : OnClickListener {
					override val onClick: () -> Unit = onClick
					override fun describeContents(): Int = 0
					override fun writeToParcel(p0: Parcel?, p1: Int) = Unit
				}
			}
		}

		@IgnoredOnParcel
		val onClick: () -> Unit get() = {}
	}
}