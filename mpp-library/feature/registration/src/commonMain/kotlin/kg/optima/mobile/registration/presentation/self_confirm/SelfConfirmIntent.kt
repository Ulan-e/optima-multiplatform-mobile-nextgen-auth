package kg.optima.mobile.registration.presentation.self_confirm

import kg.optima.mobile.base.presentation.BaseMppIntent
import kg.optima.mobile.registration.presentation.self_confirm.model.AnimationModel
import kg.optima.mobile.registration.presentation.self_confirm.model.Res

class SelfConfirmIntent(
	override val mppState: SelfConfirmState,
) : BaseMppIntent<SelfConfirmModel>() {

    fun onNext() = mppState.handle(SelfConfirmModel.NextScreen)

    fun fadeAnimationModels(mode: IdentificationMode) = when (mode) {
        IdentificationMode.SHORT -> mppState.handle(SelfConfirmModel.AnimationModels(itemsShort))
        IdentificationMode.FULL -> mppState.handle(SelfConfirmModel.AnimationModels(items))
    }
}

enum class IdentificationMode {
    SHORT,
    FULL
}

private val items = listOf(
    AnimationModel(
        durationMillis = 0,
        delayMillis = 0,
        res = Res.Passport,
        text = "Паспорта КР - ID карта (серии AN или ID)",
    ),
    AnimationModel(
        delayMillis = 1000,
        res = Res.Sun,
        text = "Хорошее освещение",
    ),
    AnimationModel(
        delayMillis = 2000,
        res = Res.Smile,
        text = "Нейтральное выражение лица",
    ),
    AnimationModel(
        delayMillis = 3000,
        res = Res.Glasses,
        text = "Отсутствие очков или друких аксессуаров на лице",
    ),
    AnimationModel(
        delayMillis = 4000,
        res = Res.FullScreen,
        text = "Лицо по центру экрана, без движений",
    ),
    AnimationModel(
        delayMillis = 5000,
        res = Res.Person,
        text = "Волосы собраны и не закрывают лицо, на камере не должно быть других людей",
    )
)
private val itemsShort = listOf(
    AnimationModel(
        delayMillis = 0,
        res = Res.Sun,
        text = "Хорошее освещение",
    ),
    AnimationModel(
        delayMillis = 0,
        res = Res.Smile,
        text = "Нейтральное выражение лица",
    ),
    AnimationModel(
        delayMillis = 0,
        res = Res.Glasses,
        text = "Отсутствие очков или друких аксессуаров на лице",
    ),
    AnimationModel(
        delayMillis = 0,
        res = Res.FullScreen,
        text = "Лицо по центру экрана, без движений",
    ),
    AnimationModel(
        delayMillis = 0,
        res = Res.Person,
        text = "Волосы собраны и не закрывают лицо, на камере не должно быть других людей",
    )
)