package kg.optima.mobile.base.presentation

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.PlatformViewModel
import kg.optima.mobile.core.error.Failure
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent

/**
 * [Intent] - In parameter using as params for launch executing,
 * [DomainModel] - In parameter,
 * [State] - Out parameter sending to StateFlow. State should be Sealed Class.
 **/
abstract class IntentHandler<in Intent, in DomainModel, out State>(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Main,
) : PlatformViewModel(), KoinComponent {

    private val coroutineScope = CoroutineScope(coroutineDispatcher + SupervisorJob())

    private val handler = CoroutineExceptionHandler { _, exception ->
        // TODO log exception to Crashlytics
        println("StateMachine CoroutineExceptionHandler got $exception")
    }

    abstract val stateMachine: StateMachine<DomainModel, State>

    abstract fun dispatch(intent: Intent)

    @Suppress("UNCHECKED_CAST")
    protected fun <T> launchOperation(
        operation: suspend CoroutineScope.() -> Either<Failure, DomainModel>,
    ): Job {
        return coroutineScope.launch(handler) {
            (stateMachine::_status).get().tryEmit(Status.SHOW_LOADING)
            withContext(
                context = Dispatchers.Main,
                block = { operation(this) }
            ).fold(
                fnL = { err -> stateMachine.setError(err) },
                fnR = { model -> stateMachine.handle(model) }
            )
        }
    }
}