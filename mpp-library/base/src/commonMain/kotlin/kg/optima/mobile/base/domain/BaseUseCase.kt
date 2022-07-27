package kg.optima.mobile.base.domain

import kg.optima.mobile.base.data.model.Either
import kg.optima.mobile.core.error.Failure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

abstract class BaseUseCase<in Model, out Result> where Result : Any {

   abstract suspend fun execute(model: Model): Either<Failure, Result>

   open suspend operator fun invoke(
	  scope: CoroutineScope,
	  params: Model,
   ): Either<Failure, Result> {
	  val deferred = scope.async { execute(params) }
	  return withContext(scope.coroutineContext) {
		 try {
			deferred.await()
		 } catch (e: Exception) {
			Either.Left(Failure.UseCaseError)
		 }
	  }
   }

}