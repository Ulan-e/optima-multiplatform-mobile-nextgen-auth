package kg.optima.mobile.network.failure

interface NetworkFailure {

    fun getDefaultFailure(): Throwable
    fun getUnknownException(): Throwable
    fun getBaseFailure(errorResponse: String): Throwable

    fun getBadRequestException(): Throwable
    fun getUnauthorizedException(): Throwable
    fun getNotFoundFailure(): Throwable
}