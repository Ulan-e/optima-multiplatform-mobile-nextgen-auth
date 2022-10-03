package kg.optima.mobile.registration.presentation.offer

import kg.optima.mobile.base.presentation.BaseMppIntent

class OfferIntent(
	override val mppState: OfferState
) : BaseMppIntent<Unit>()