package kg.optima.mobile.base.domain

import kg.optima.mobile.network.failure.BaseFailure
import kg.optima.mobile.network.failure.Failure
import kg.optima.mobile.base.data.model.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

abstract class BaseUseCase<in Params, out Type> where Type : Any {

   abstract suspend fun execute(params: Params, scope: CoroutineScope): Either<Failure, Type>

   open suspend operator fun invoke(
	  scope: CoroutineScope,
	  params: Params,
   ): Either<Failure, Type> {
	  val deferred = scope.async { execute(params, this) }
	  return withContext(scope.coroutineContext) {
		 try {
			deferred.await()
		 } catch (e: Exception) {
			Either.Left(BaseFailure.UseCaseError)
		 }
	  }
   }

}