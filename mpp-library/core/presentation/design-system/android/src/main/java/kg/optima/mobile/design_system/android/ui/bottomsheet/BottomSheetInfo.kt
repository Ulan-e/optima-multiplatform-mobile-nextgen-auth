package kg.optima.mobile.design_system.android.ui.bottomsheet

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kotlinx.parcelize.IgnoredOnParcel

@Parcelize
class BottomSheetInfo(
	val title: String,
	val description: String? = null,
	val composableContent: ComposableContent = ComposableContent.Default,
	val buttons: List<ButtonView>,
) : Parcelable {
	interface ComposableContent : Parcelable {
		@IgnoredOnParcel val content: @Composable () -> Unit

		companion object {
			fun composableContent(content: @Composable () -> Unit): ComposableContent {
				return object : ComposableContent {
					override val content: @Composable () -> Unit = content
					override fun describeContents(): Int = 0
					override fun writeToParcel(p0: Parcel?, p1: Int) = Unit

				}
			}

			val Default = composableContent {}
		}
	}
}