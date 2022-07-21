package kg.optima.mobile.network.failure

import kotlinx.serialization.json.Json

class NetworkFailureImpl(private val json: Json) : NetworkFailure {

    override fun getDefaultFailure(): Throwable {
        return BaseFailure.Default
    }

    override fun getNotFoundFailure(): Throwable {
        return BaseFailure.NotFoundFailure
    }

    override fun getBadRequestException(): Throwable {
        return BaseFailure.BadRequestException
    }

    override fun getUnknownException(): Throwable {
        return BaseFailure.UnknownException
    }

    override fun getBaseFailure(errorResponse: String): Throwable {
        val exception = json.decodeFromString(
            ApiError.serializer(),
            errorResponse
        )
        return BaseFailure.Message(exception.message ?: "Network Error")
    }
}