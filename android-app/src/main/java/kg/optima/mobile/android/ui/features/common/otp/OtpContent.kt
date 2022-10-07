package kg.optima.mobile.android.ui.features.common.otp

import android.text.format.DateUtils
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kg.optima.mobile.android.ui.base.MainContainer
import kg.optima.mobile.base.presentation.UiState
import kg.optima.mobile.core.common.Constants
import kg.optima.mobile.design_system.android.ui.bottomsheet.BottomSheetInfo
import kg.optima.mobile.design_system.android.ui.buttons.PrimaryButton
import kg.optima.mobile.design_system.android.ui.buttons.model.ButtonView
import kg.optima.mobile.design_system.android.ui.input.CodeInput
import kg.optima.mobile.design_system.android.ui.text_fields.TitleTextField
import kg.optima.mobile.design_system.android.utils.resources.ComposeColors
import kg.optima.mobile.design_system.android.utils.resources.sp
import kg.optima.mobile.design_system.android.values.Deps
import kg.optima.mobile.resources.Headings

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OtpContent(
	model: UiState.Model?,
	description: String,
	phoneNumber: String,
	code: String,
	timeLeft: Int,
	error: String,
	triesCount: Int,
	onStartTimer: () -> Unit,
	onPauseTimer: () -> Unit,
	onValueChanged: (String) -> Unit,
	onInputCompleted: (String) -> Unit,
	onButtonClicked: () -> Unit,
) {
	val bottomSheetState = remember { mutableStateOf<BottomSheetInfo?>(null) }

	when (model) {
		is UiState.Model.Initial -> onStartTimer()
		is UiState.Model.Error -> {
			if (triesCount <= 0) {
				onPauseTimer()
				bottomSheetState.value = BottomSheetInfo(
					title = "Вы ввели код неверно несколько раз. Попробуйте запросить новый код",
					buttons = listOf(
						ButtonView.Primary(
							text = "Закрыть",
							onClickListener = ButtonView.onClickListener {
								onStartTimer()
								bottomSheetState.value = null
							},
						)
					)
				)
			}
		}
	}

	MainContainer(
		mainState = model,
		sheetInfo = bottomSheetState.value,
		scrollable = true,
		contentHorizontalAlignment = Alignment.Start,
	) {
		Spacer(modifier = Modifier.weight(1f))
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.align(Alignment.CenterHorizontally),
		) {
			TitleTextField(text = "Введите код подтверждения")
			Text(
				modifier = Modifier.padding(top = Deps.Spacing.colElementMargin),
				text = description,
				fontSize = Headings.H5.sp,
				fontWeight = FontWeight.Medium,
				lineHeight = Deps.TextSize.lineHeight,
				color = ComposeColors.DescriptionGray
			)
			Text(
				modifier = Modifier.padding(top = Deps.Spacing.minPadding * 2),
				text = phoneNumber,
				fontSize = Headings.H5.sp,
				fontWeight = FontWeight.Bold,
			)
		}
		CodeInput(
			modifier = Modifier.padding(top = Deps.Spacing.standardMargin * 2),
			value = code,
			showValue = true,
			onValueChanged = onValueChanged,
			onInputCompleted = onInputCompleted,
			withKeyboard = true,
			showKeyboardPermanently = true,
			isValid = error != Constants.OTP_INVALID_ERROR_CODE,
		)

		if (error == Constants.OTP_INVALID_ERROR_CODE) {
			Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
				Text(
					modifier = Modifier.padding(vertical = Deps.Spacing.standardMargin),
					text = "Неверный Код. ",
					fontSize = Headings.H4.sp,
					fontWeight = FontWeight.Normal,
					color = ComposeColors.PrimaryRed
				)
				Text(
					modifier = Modifier.padding(vertical = Deps.Spacing.standardMargin),
					text = "Осталось попыток: $triesCount",
					fontSize = Headings.H4.sp,
					fontWeight = FontWeight.Normal,
					color = ComposeColors.DescriptionGray
				)
			}
		}
		Spacer(modifier = Modifier.weight(2f))
		PrimaryButton(
			modifier = Modifier.fillMaxWidth(),
			text = buttonTextFormatter(timeLeft),
			color = ComposeColors.Green,
			onClick = onButtonClicked,
			enabled = timeLeft == 0,
		)
	}
}

private fun buttonTextFormatter(time: Int): String {
	return when {
		time <= 0 -> "Отправить повторно"
		time <= 60 -> "Запросить через $time сек."
		else -> "Запросить через ${DateUtils.formatElapsedTime(time.toLong())}"
	}
}