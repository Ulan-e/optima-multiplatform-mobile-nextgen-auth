package kg.optima.mobile.android

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kg.optima.mobile.BuildConfig
import kg.optima.mobile.android.ui.base.routing.Router
import kg.optima.mobile.android.ui.base.routing.RouterImpl
import kg.optima.mobile.di.Injector
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
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

	private val navigationModule = module {
		factory<Router> { RouterImpl }
	}

	private fun initFirebase() {
		FirebaseApp.initializeApp(this)
		FirebaseCrashlytics.getInstance()
			.setCrashlyticsCollectionEnabled(BuildConfig.DEBUG.not())
	}
}