package kg.optima.mobile.storage

import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module

object StorageFactory {
    val module: Module = module {
        factory { Settings() }
        factory { StorageImpl(get(), get()) }
    }
}