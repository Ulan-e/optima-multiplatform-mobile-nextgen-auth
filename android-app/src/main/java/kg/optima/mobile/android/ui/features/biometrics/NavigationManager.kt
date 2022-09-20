package kg.optima.mobile.android.ui.features.biometrics

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kg.optima.mobile.android.ui.SingleActivity
import kg.optima.mobile.core.navigation.ScreenModel
import kg.optima.mobile.feature.registration.RegistrationScreenModel

object NavigationManager {

    fun Context.navigateTo(activity: AppCompatActivity) {
        val intent = Intent(this, activity::class.java)
        this.startActivity(intent)
    }

    fun Context.navigateTo(screenModel: ScreenModel) {
        val intent = Intent(this, SingleActivity::class.java)
        intent.putExtra(SingleActivity.NEXT_SCREEN_MODEL, screenModel)
        this.startActivity(intent)
    }
}