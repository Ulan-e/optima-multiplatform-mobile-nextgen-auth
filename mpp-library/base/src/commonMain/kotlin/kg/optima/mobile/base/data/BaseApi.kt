package kg.optima.mobile.base.data

import io.ktor.http.*
import kg.optima.mobile.core.StringMap
import kg.optima.mobile.network.client.NetworkClient
import kotlinx.serialization.KSerializer

abstract class BaseApi(
	val networkClient: NetworkClient,
) {
	abstract val baseUrl: String

	protected val userAgent: String
		get() {
			return "Optima24/1.0 (Android; Samsung Galaxy S21 Ultra/000000000000000)"
//			return format(
//				format = "%s (%s; %s/%s)",
//				"Optima24/1.0",
//				PlatformInfo.os,
//				PlatformInfo.deviceModel,
//				"000000000000000"
//			)
		}

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

	suspend inline fun <Request : Any?, reified V> post(
		path: String,
		noinline headers: HeadersBuilder.() -> Unit = {},
		request: Request,
	): V {
		return networkClient.post(baseUrl, path, headers, request)
	}

	suspend inline fun <reified V> get(
		path: String,
		noinline headers: HeadersBuilder.() -> Unit = {},
		params: StringMap = mapOf(),
	): V {
		return networkClient.get(baseUrl, path, headers, params)
	}
}