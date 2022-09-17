package kg.optima.mobile.design_system.android.utils.resources

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.resources.Colors

object ComposeColors {
	val PrimaryRed: Color get() = Colors.PrimaryRed.toComposeColor()
	val PrimaryDisabledGray: Color get() = Colors.PrimaryDisabledGray.toComposeColor()
	val PrimaryWhite: Color get() = Colors.PrimaryWhite.toComposeColor()
	val PrimaryBlack: Color get() = Colors.PrimaryBlack.toComposeColor()
	val PrimaryLightGray: Color get() = Colors.PrimaryLightGray.toComposeColor()
	val Dark: Color get() = Colors.Dark.toComposeColor()

	val Background: Color get() = Colors.SecondaryBackground.toComposeColor()
	val Green: Color get() = Colors.Green.toComposeColor()
	val SystemGreen: Color get() = Colors.SystemGreen.toComposeColor()
	val DescriptionGray: Color get() = Colors.DescriptionGray.toComposeColor()
	val WhiteF5: Color get() = Colors.WhiteF5.toComposeColor()

	val OpaquedLightGray80: Color get() = PrimaryLightGray.copy(alpha = 0.8f)
	val OpaquedDisabledGray: Color get() = DescriptionGray.copy(alpha = 0.08f)
	val OpaquedDisabledGray20: Color get() = DescriptionGray.copy(alpha = 0.2f)
}

@Parcelize
class ComposeColor(
	val colorParameter: ColorParameter
) : Parcelable {
	companion object {
		fun composeColor(color: Color) = ComposeColor(
			object : ColorParameter {
				override val color: Color = color
				override fun describeContents(): Int = 0
				override fun writeToParcel(parcel: Parcel?, p1: Int) {
					parcel?.writeString(color.toString())
				}
			}
		)
	}

	interface ColorParameter : Parcelable {
		val color: Color
	}
}