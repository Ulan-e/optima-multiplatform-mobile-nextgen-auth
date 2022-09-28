

package kg.optima.mobile.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
//import kg.optima.mobile.SharedFactory
import kg.optima.mobile.feature.auth.di.AuthFactory

@InstallIn(ActivityComponent::class)
@Module
object FactoriesModule {
//    @Provides
//    fun authNewFactory(sharedFactory: SharedFactory): AuthFactory = sharedFactory.authFactory
}
