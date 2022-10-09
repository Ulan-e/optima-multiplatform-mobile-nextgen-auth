package kg.optima.mobile.android

import android.app.Application
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import kg.optima.mobile.android.ui.base.routing.Router
import kg.optima.mobile.android.ui.base.routing.RouterImpl
import kg.optima.mobile.di.Injector
import kz.optimabank.optima24.local.repository.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import timber.log.Timber

class OptimaApp : Application() {

	private var urgentMessageState = false

	override fun onCreate() {
		super.onCreate()
		instance = this
//        initFirebase()
		initDi()

		initLegacyProject(this)
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

	private fun initLegacyProject(application: Application) {
		appDatabase = AppDatabase.getInstance(this)

		// для перехвата ошибок в RxJava, когда реактивные процессы не закончены
		RxJavaPlugins.setErrorHandler { error: Throwable ->
			Timber.e("rxJava error" + error.localizedMessage)
		}

		val builder = Picasso.Builder(this)
		builder.downloader(OkHttp3Downloader(application, Long.MAX_VALUE))
		val built = builder.build()
		built.setIndicatorsEnabled(true)
		built.isLoggingEnabled = true
		Picasso.setSingletonInstance(built)

	}

	fun getUrgentMessageState(): Boolean {
		return urgentMessageState
	}

	fun changeUrgentMessageState(enableUrgentMessage: Boolean) {
		urgentMessageState = enableUrgentMessage
	}

	companion object {
		lateinit var instance: OptimaApp
			private set

		lateinit var appDatabase: AppDatabase
			private set
	}
}