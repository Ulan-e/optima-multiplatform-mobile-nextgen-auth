package kg.optima.mobile.core.error

sealed class Failure(
    override val message: String = ""
) : Throwable() {

    object Default : Failure()
    object UnknownException : Failure()
    class Message(override val message: String) : Failure()

    object BadRequestException : Failure()
    object UnauthorizedException : Failure()
    object NotFoundFailure : Failure()

    object SocketTimeoutException : Failure()
    object ClientRequestException : Failure()
    object UseCaseError : Failure()

}