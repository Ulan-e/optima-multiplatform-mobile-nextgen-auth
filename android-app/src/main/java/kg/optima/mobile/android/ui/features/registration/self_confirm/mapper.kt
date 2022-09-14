package kg.optima.mobile.android.ui.features.registration.self_confirm

import kg.optima.mobile.design_system.android.ui.animation.FadeInAnimModel
import kg.optima.mobile.design_system.android.utils.resources.resId
import kg.optima.mobile.registration.presentation.self_confirm.model.AnimationModel
import kg.optima.mobile.registration.presentation.self_confirm.model.Res
import kg.optima.mobile.resources.images.RegistrationImages

fun List<AnimationModel>.toUiModel(): List<FadeInAnimModel> {
    return map { it.toUiModel() }
}

private fun AnimationModel.toUiModel(): FadeInAnimModel {
    return FadeInAnimModel(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
        resId = res.resId(),
        text = text,
    )
}

private fun Res.resId(): Int {
    val imageResource = when (this) {
        Res.Passport -> RegistrationImages.passport
        Res.Sun -> RegistrationImages.sun
        Res.Smile -> RegistrationImages.smile
        Res.Glasses -> RegistrationImages.glasses
        Res.FullScreen -> RegistrationImages.fullscreen
        Res.Person -> RegistrationImages.person
    }

    return imageResource.resId()
}