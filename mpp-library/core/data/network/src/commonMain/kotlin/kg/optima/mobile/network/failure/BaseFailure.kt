package kg.optima.mobile.network.failure

sealed class BaseFailure : Failure() {

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