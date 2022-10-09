package kg.optima.mobile.android.ui.features.optima24

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.essenty.parcelable.Parcelize
import kg.optima.mobile.android.ui.base.BaseScreen
import kg.optima.mobile.android.utils.navigateTo
import kz.optimabank.optima24.activity.ChangeAppLangActivity
import kz.optimabank.optima24.activity.MenuActivity
import kz.optimabank.optima24.activity.NavigationActivity
import kz.optimabank.optima24.notifications.ui.NotificationsActivity
import kz.optimabank.optima24.utility.Constants

@Parcelize
class Optima24Screen(
    val model: Optima24Model
) : BaseScreen {

    companion object {
        private const val mapScreen = "isMap"
        private const val rateScreen = "isRate"
    }

    @Composable
    override fun Content() {
        val context = LocalContext.current

        when (model) {
            is Optima24Model.MainPage -> context.navigateTo(MenuActivity())
            is Optima24Model.OnMap -> toNavigationActivity(context, mapScreen)
            is Optima24Model.Rates -> toNavigationActivity(context, rateScreen)
            is Optima24Model.Language -> context.navigateTo(ChangeAppLangActivity())
            is Optima24Model.Notification -> toNotificationActivity(context, model.notificationId)
        }
    }

    private fun toNavigationActivity(context: Context, value: String) {
        val intent = Intent(context, NavigationActivity::class.java)
        intent.putExtra(value, true)
        context.navigateTo(intent)
    }

    private fun toNotificationActivity(context: Context, notificationId: String) {
        val intent = Intent(context, NotificationsActivity::class.java)
        intent.putExtra(Constants.IS_NOTIFICATION, true)
        intent.putExtra(Constants.NOTIFICATION_ARG_ID, notificationId)
        context.navigateTo(intent)
    }
}