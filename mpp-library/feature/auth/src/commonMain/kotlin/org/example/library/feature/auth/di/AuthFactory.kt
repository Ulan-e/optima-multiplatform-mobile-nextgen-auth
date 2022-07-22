/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package kg.optima.mobile.feature.auth.di

import dev.icerock.moko.errors.handler.ExceptionHandler
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import kg.optima.mobile.feature.auth.model.ServerApi
import kg.optima.mobile.feature.auth.presentation.AuthViewModel

class AuthFactory(
    private val createExceptionHandler: () -> ExceptionHandler,
    private val serverApi: ServerApi
) {
    fun createAuthViewModel(
        eventsDispatcher: EventsDispatcher<AuthViewModel.EventsListener>
    ) = AuthViewModel(
        eventsDispatcher = eventsDispatcher,
        createExceptionHandler(),
        serverApi
    )
}
