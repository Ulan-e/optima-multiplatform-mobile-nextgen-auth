package kg.optima.mobile.auth.domain

import kg.optima.mobile.auth.data.api.AuthApi
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.base.data.model.getRight
import kg.optima.mobile.base.data.model.map
import kg.optima.mobile.base.domain.BaseUseCase
import kg.optima.mobile.network.failure.Failure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthUseCase(
    private val authApi: AuthApi
) : BaseUseCase<AuthUseCase.Param, String>() {

    class Param

    override suspend fun execute(
        params: Param,
        scope: CoroutineScope
    ): Either<Failure, String> {
        return authApi.auth().map {
            it
        }
    }

    suspend fun gg(): String {
        return authApi.auth().getRight()
    }
}