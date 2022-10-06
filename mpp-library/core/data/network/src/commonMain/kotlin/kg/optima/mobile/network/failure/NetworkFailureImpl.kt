package kg.optima.mobile.network.failure

import kg.optima.mobile.core.error.Failure
import kotlinx.serialization.json.Json

class NetworkFailureImpl(private val json: Json) : NetworkFailure {

    override fun getDefaultFailure() = Failure.Default

    override fun getUnknownException() = Failure.UnknownException

    override fun getBaseFailure(errorResponse: String): Throwable {
        val exception = json.decodeFromString(ApiError.serializer(), errorResponse)
        return Failure.Message(exception.message ?: "Network Error")
    }

    override fun getBadRequestException() = Failure.BadRequestException

    override fun getUnauthorizedException() = Failure.UnauthorizedException

    override fun getNotFoundFailure() = Failure.NotFoundFailure
}