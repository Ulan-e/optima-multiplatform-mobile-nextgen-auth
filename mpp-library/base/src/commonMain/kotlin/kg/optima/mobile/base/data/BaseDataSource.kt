package kg.optima.mobile.base.data

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure

/**
 * Базовый класс для запросов в сеть и в локал
 */
abstract class BaseDataSource {

	companion object {
		val STATUS_OK = 200..299

		const val UNAUTHORIZED = 401
		const val FORBIDDEN = 403
		const val UNPROCESSABLE_ENTITY = 422
		const val TOO_MANY_REQUEST = 429
		const val SERVER_UPDATE_IN_PROGRESS = 499
	}

	protected inline fun <reified T : Any?, reified R : Any> apiCall(
		call: () -> T,
		mapResponse: (T) -> R,
	): Either<Failure, R> {
		return try {
			Either.Right(mapResponse(call.invoke()))
		} catch (response: Failure.ClientRequestException) {
			Either.Left(Failure.ClientRequestException)
		} catch (exception: Failure.SocketTimeoutException) {
			Either.Left(Failure.SocketTimeoutException)
		} catch (ex: Failure) {
			Either.Left(ex)
		}
	}

	protected inline fun <reified T : Any?> apiCall(
		call: () -> T,
	): Either<Failure, T> {
		return try {
			Either.Right(call.invoke())
		} catch (response: Failure.ClientRequestException) {
			Either.Left(Failure.ClientRequestException)
		} catch (exception: Failure.SocketTimeoutException) {
			Either.Left(Failure.SocketTimeoutException)
		} catch (ex: Failure) {
			Either.Left(ex)
		}
	}

	protected inline fun <reified T> localCall(
		call: () -> T,
	): Either<Failure, T> {
		return try {
			Either.Right(call.invoke())
		} catch (ex: Failure) {
			Either.Left(ex)
		}
	}

	protected inline fun <reified T, reified R : Any> localCall(
		call: () -> T,
		mapResponse: (T) -> R,
	): Either<Failure, R> {
		return try {
			Either.Right(mapResponse(call.invoke()))
		} catch (ex: Failure) {
			Either.Left(ex)
		}
	}

}
