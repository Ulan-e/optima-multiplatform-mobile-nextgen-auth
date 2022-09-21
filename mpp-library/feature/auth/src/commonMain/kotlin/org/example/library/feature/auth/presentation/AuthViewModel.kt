package org.example.library.feature.auth.presentation

import dev.icerock.moko.errors.handler.ExceptionHandler
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcherOwner
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch
import kg.optima.mobile.feature.auth.model.ServerApi

class AuthViewModel(
    override val eventsDispatcher: EventsDispatcher<EventsListener>,
    val exceptionHandler: ExceptionHandler,
    private val serverApi: ServerApi
) : ViewModel(), EventsDispatcherOwner<AuthViewModel.EventsListener> {

    val login: MutableLiveData<String> = MutableLiveData("")
    val password: MutableLiveData<String> = MutableLiveData("")

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun onSubmitPressed() {
        viewModelScope.launch {
            _isLoading.value = true
            exceptionHandler.handle {
                serverApi.login(login = login.value, password = password.value)

                eventsDispatcher.dispatchEvent { routeSuccessAuth() }
            }.finally {
                _isLoading.value = false
            }.execute()
        }
    }

    interface EventsListener {
        fun routeSuccessAuth()
    }
}
