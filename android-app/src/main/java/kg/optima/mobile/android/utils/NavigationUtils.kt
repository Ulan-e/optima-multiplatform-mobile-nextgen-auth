package kg.optima.mobile.android.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kg.optima.mobile.android.ui.SingleActivity
import kg.optima.mobile.android.ui.base.BaseActivity
import kg.optima.mobile.base.presentation.UiState

fun Context.navigateTo(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    this.startActivity(intent)
}

fun Context.navigateTo(stateModel: UiState.Model.Navigate) {
    val intent = Intent(this, SingleActivity::class.java)
    intent.putExtra(BaseActivity.TARGET_NAVIGATE_MODEL, stateModel)
    this.startActivity(intent)
}