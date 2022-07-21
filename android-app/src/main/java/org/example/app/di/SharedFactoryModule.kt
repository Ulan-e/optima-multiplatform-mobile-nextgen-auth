package org.example.app.di

import com.russhwolf.settings.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.aakira.napier.Antilog
import org.example.app.BuildConfig
//import org.example.library.SharedFactory
import javax.inject.Singleton

/**
 * Module, that provides shared mutliplatform factory for Android platform
 */
@InstallIn(SingletonComponent::class)
@Module
object SharedFactoryModule {
//    @Provides
//    @Singleton
//    fun provideMultiplatformFactory(
//        settings: Settings,
//        antilog: Antilog,
//    ): SharedFactory = SharedFactory(
//        settings = settings,
//        antilog = antilog,
//        baseUrl = BuildConfig.BASE_URL
//    )
}
