package kg.optima.mobile.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import javax.inject.Singleton

/**
 * module to provide Antilog for multiplatform factory
 */
@InstallIn(SingletonComponent::class)
@Module
object AntilogModule {
    @Provides
    @Singleton
    fun provideAntilog(): Antilog = DebugAntilog()
}
