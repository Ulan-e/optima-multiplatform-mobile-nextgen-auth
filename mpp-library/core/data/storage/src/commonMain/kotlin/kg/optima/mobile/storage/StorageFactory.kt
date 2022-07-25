package kg.optima.mobile.storage

import com.russhwolf.settings.Settings
import kg.optima.mobile.storage.cache.RuntimeCache
import org.koin.core.module.Module
import org.koin.dsl.module

object StorageFactory {
    val module: Module = module {
        factory { Settings() }
        factory<StorageRepository> { StorageRepositoryImpl(get(), get()) }
        factory { RuntimeCache() }
    }
}