package kg.optima.mobile.android.ui.features.registration.control_question

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kg.optima.mobile.android.ui.FeatureRouter
import kg.optima.mobile.android.ui.features.registration.create_password.CreatePasswordModel
import kg.optima.mobile.android.ui.features.registration.create_password.CreatePasswordScreen
import kg.optima.mobile.registration.presentation.control_question.ControlQuestionState

object ControlQuestionRouter : FeatureRouter<ControlQuestionState.Model.NavigateTo> {
	@Composable
	override fun compose(stateModel: ControlQuestionState.Model.NavigateTo): Screen {
		return when (stateModel) {
			is ControlQuestionState.Model.NavigateTo.CreatePassword ->
				CreatePasswordScreen(
					createPasswordModel = CreatePasswordModel(
						hash = stateModel.hash,
						questionId = stateModel.questionId,
						answer = stateModel.answer
					)
				)
		}
	}
}