package kg.optima.mobile.core.error

sealed class Failure(
    override val message: String = ""
) : Throwable() {

    object Default : Failure()

    class Message(override val message: String) : Failure()

    object UnknownException : Failure()

    object BadRequestException : Failure()

    class ApiCodeFailure(override val message: String) : Failure()
    class ApiMessageFailure(override val message: String) : Failure()
    object ApiDataFailure : Failure()
    object RepositoryFailure : Failure()

    object NotFoundFailure : Failure()
    object NullPointFailure : Failure()

    object SocketTimeoutException : Failure()
    object ClientRequestException : Failure()
    object UseCaseError : Failure()

}