package kg.optima.mobile.android.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.FragmentActivity
import kg.optima.mobile.base.presentation.UiState

abstract class BaseActivity : FragmentActivity() {

	companion object {
		const val TARGET_NAVIGATE_MODEL =
			"kg.optima.mobile.android.ui.BaseActivity:TargetNavigateModel"
	}

	protected var nextScreenModel: UiState.Model.Navigate? = null

	lateinit var navigationController: NavigationController

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		navigationController = NavigationController(savedInstanceState)
		nextScreenModel = intent.getParcelableExtra(TARGET_NAVIGATE_MODEL)
	}

	override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
		super.onSaveInstanceState(outState, outPersistentState)
		navigationController.onSaveInstanceState(outState)
	}
}