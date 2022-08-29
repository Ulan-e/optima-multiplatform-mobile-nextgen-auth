package kg.optima.mobile.android

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kg.optima.mobile.BuildConfig
import kg.optima.mobile.android.ui.base.Router
import kg.optima.mobile.android.ui.base.RouterImpl
import kg.optima.mobile.di.Injector
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

class OptimaApp : Application() {

	override fun onCreate() {
		super.onCreate()
//        initFirebase()
		initDi()
	}

	private fun initDi() {
		Injector.initKoin {
			androidContext(this@OptimaApp)
			loadKoinModules(listOf(navigationModule))
		}
	}

	private val navigationModule: Module = module {
		factory<Router> { RouterImpl }
	}

	private fun initFirebase() {
		FirebaseApp.initializeApp(this)
		FirebaseCrashlytics.getInstance()
			.setCrashlyticsCollectionEnabled(BuildConfig.DEBUG.not())
	}
}