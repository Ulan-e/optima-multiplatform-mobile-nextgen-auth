package kg.optima.mobile.design_system.android.ui.bottomsheet

import android.os.Parcelable
import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView

@Parcelize
class BottomSheetInfo(
	val title: String,
	val description: String? = null,
	val content: @Composable () -> Unit = {},
	val buttons: List<ButtonView>,
) : Parcelable