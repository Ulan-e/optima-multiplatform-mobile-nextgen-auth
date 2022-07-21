package kg.optima.mobile.network.failure

interface NetworkFailure {

    fun getDefaultFailure(): Throwable
    fun getNotFoundFailure(): Throwable
    fun getBadRequestException(): Throwable
    fun getUnknownException(): Throwable
    fun getBaseFailure(errorResponse: String): Throwable
}