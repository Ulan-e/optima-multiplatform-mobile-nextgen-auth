package kg.optima.mobile.android.ui.features.biometrics

import androidx.compose.foundation.layout.fillMaxWidth
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
    positiveButton: () -> Unit,
    negativeButton: () -> Unit,
): BottomSheetInfo {

    return BottomSheetInfo(
        title = "Вы действительно хотите \nостановить идентификацию?",
        content = {
            Text(
                modifier = Modifier.padding(subheaderMargin),
                text = "Идентификация не закончена. \nПроцесс " +
                        "будет остановлен и вы окажетесь \nна начальном экране",
                fontSize = Headings.H4.px.sp,
                textAlign = TextAlign.Center
            )
        },
        buttons = listOf(
            ButtonView.Primary(
                modifier = Modifier.fillMaxWidth(),
                text = "Остановить процесс",
                onClick = { positiveButton.invoke() },
            ),
            ButtonView.Transparent(
                modifier = Modifier.fillMaxWidth(),
                text = "Отмена",
                onClick = { negativeButton.invoke() },
            )
        )
    )
}