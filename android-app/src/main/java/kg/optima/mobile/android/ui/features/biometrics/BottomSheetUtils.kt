package kg.optima.mobile.android.ui.features.biometrics

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.values.Deps.Spacing.subheaderMargin
import kg.optima.mobile.resources.Headings

fun showBottomSheetDialog(
    title: String,
    subTitle: String? = null,
    positiveButtonView: ButtonView? = null,
    negativeButtonView: ButtonView? = null,
): BottomSheetInfo {

    when {
        positiveButtonView != null && negativeButtonView != null -> {
            return BottomSheetInfo(
                title = title,
                content = {
                    if (subTitle != null) {
                        Text(
                            modifier = Modifier.padding(subheaderMargin),
                            text = subTitle,
                            fontSize = Headings.H4.px.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                buttons = listOf(
                    positiveButtonView,
                    negativeButtonView
                ),
            )
        }
        positiveButtonView != null -> {
            return BottomSheetInfo(
                title = title,
                content = {
                    if (subTitle != null) {
                        Text(
                            modifier = Modifier.padding(subheaderMargin),
                            text = subTitle,
                            fontSize = Headings.H4.px.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                buttons = listOf(
                    positiveButtonView,
                ),
            )
        }
        negativeButtonView != null -> {
            return BottomSheetInfo(
                title = title,
                content = {
                    if (subTitle != null) {
                        Text(
                            modifier = Modifier.padding(subheaderMargin),
                            text = subTitle,
                            fontSize = Headings.H4.px.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                buttons = listOf(
                    negativeButtonView,
                ),
            )
        }
        else -> {
            return BottomSheetInfo(
                title = title,
                content = {
                    if (subTitle != null) {
                        Text(
                            modifier = Modifier.padding(subheaderMargin),
                            text = subTitle,
                            fontSize = Headings.H4.px.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                buttons = listOf(),
            )
        }
    }
}