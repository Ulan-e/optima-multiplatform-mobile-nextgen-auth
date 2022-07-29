package kg.optima.mobile.base.data

import io.ktor.http.*
import kg.optima.mobile.core.StringMap
import kg.optima.mobile.network.client.NetworkClient
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass


abstract class BaseApi(
	val networkClient: NetworkClient,
) {
	abstract val baseUrl: String

	suspend inline fun <R, reified V> request(
		path: String,
		body: Pair<R, KSerializer<R>>? = null,
		httpMethod: HttpMethod = HttpMethod.Post,
	): V {
		return networkClient.request(baseUrl, path, body, httpMethod)
	}

	suspend inline fun <reified V> post(
		path: String,
		params: StringMap = mapOf(),
	): V {
		return networkClient.post(baseUrl, path, params)
	}
}