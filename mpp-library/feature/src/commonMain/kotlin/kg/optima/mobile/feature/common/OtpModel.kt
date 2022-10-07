package kg.optima.mobile.feature.common

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
open class OtpModel<T : Parcelable>(
	open val phoneNumber: String,
	open val entity: T,
	open val timeLeft: Long = 0L,
) : Parcelable