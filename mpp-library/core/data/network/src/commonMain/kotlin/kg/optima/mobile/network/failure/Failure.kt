package kg.optima.mobile.network.failure

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
/**
 * Базовые ошибки приложения
 */
abstract class Failure(override val message: String = "") : Throwable()