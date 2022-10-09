package kg.optima.mobile.android.ui.features.optima24

import android.os.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class Optima24Model : Parcelable {

    @Parcelize
    object MainPage : Optima24Model()

    @Parcelize
    object Language : Optima24Model()

    @Parcelize
    object OnMap : Optima24Model()

    @Parcelize
    object Rates : Optima24Model()

    @Parcelize
    data class Notification(
        val notificationId: String
    ) : Optima24Model()
}