package kg.optima.mobile.base.data

import kg.optima.mobile.base.data.model.BaseDto
import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure
import kg.optima.mobile.network.const.NetworkCode


abstract class BaseDataSource {

	protected inline fun <reified T : Any?> apiCall(
		call: () -> BaseDto<T>,
	): Either<Failure, BaseDto<T>> {
		return try {
			val response = call.invoke()
			validate(response)
		} catch (response: Failure.ClientRequestException) {
			Either.Left(Failure.ClientRequestException)
		} catch (exception: Failure.SocketTimeoutException) {
			Either.Left(Failure.SocketTimeoutException)
		} catch (ex: Failure) {
			Either.Left(ex)
		}
	}

	protected inline fun <reified T : Any?, reified R : Any> apiCall(
		call: () -> BaseDto<T>,
		mapResponse: (BaseDto<T>) -> BaseDto<R>,
	): Either<Failure, BaseDto<R>> {
		return try {
			val response = mapResponse(call.invoke())
			validate(response)
		} catch (response: Failure.ClientRequestException) {
			Either.Left(Failure.ClientRequestException)
		} catch (exception: Failure.SocketTimeoutException) {
			Either.Left(Failure.SocketTimeoutException)
		} catch (ex: Failure) {
			Either.Left(ex)
		}
	}

	protected fun <T> validate(response: BaseDto<T>): Either<Failure, BaseDto<T>> {
		return when (NetworkCode.byCode(response.code)) {
			NetworkCode.ServiceUnavailable -> Either.Left(Failure.BadRequestException)
			else -> Either.Right(response)
		}
	}

}
