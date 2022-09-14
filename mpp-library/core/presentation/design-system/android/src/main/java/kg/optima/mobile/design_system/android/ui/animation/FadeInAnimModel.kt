package kg.optima.mobile.design_system.android.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kg.optima.mobile.design_system.android.values.Deps

@Composable
fun FadeInAnim(model: FadeInAnimModel) {
	AnimatedVisibility(
		modifier = model.modifier,
		visibleState = remember {
			MutableTransitionState(initialState = false)
		}.also { it.targetState = true },
		enter = fadeIn(
			animationSpec = tween(
				durationMillis = model.durationMillis,
				delayMillis = model.delayMillis,
			)
		),
		content = model.content,
	)
}

class FadeInAnimModel(
	val durationMillis: Int = 500,
	val delayMillis: Int,
	val resId: Int,
	val text: String,
) {
	val modifier = Modifier.padding(bottom = Deps.Spacing.spacing)
	val content: @Composable() AnimatedVisibilityScope.() -> Unit = {
		IconTextFiled(resId = resId, text = text)
	}
}