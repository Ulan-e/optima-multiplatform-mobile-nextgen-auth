package kg.optima.mobile.network.failure

import kg.optima.mobile.core.error.Failure
import kotlinx.serialization.json.Json

class NetworkFailureImpl(private val json: Json) : NetworkFailure {

    override fun getDefaultFailure(): Throwable {
        return Failure.Default
    }

    override fun getNotFoundFailure(): Throwable {
        return Failure.NotFoundFailure
    }

    override fun getBadRequestException(): Throwable {
        return Failure.BadRequestException
    }

    override fun getUnknownException(): Throwable {
        return Failure.UnknownException
    }

    override fun getBaseFailure(errorResponse: String): Throwable {
        val exception = json.decodeFromString(
            ApiError.serializer(),
            errorResponse
        )
        return Failure.Message(exception.message ?: "Network Error")
    }
}