package kg.optima.mobile.network.client

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kg.optima.mobile.core.StringMap
import kg.optima.mobile.network.client.ext.setBody
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

abstract class NetworkClient {
	abstract val httpClient: HttpClient
	abstract val json: Json

	suspend inline fun <Request, reified Response> request(
		baseUrl: String,
		path: String,
		body: Pair<Request, KSerializer<Request>>?,
		httpMethod: HttpMethod,
	): Response = httpClient.request {
		url {
			takeFrom(baseUrl)
			path(path)
			method = httpMethod
			contentType(ContentType.Application.FormUrlEncoded)
			if (body != null) setBody(body.second, body.first, json)
		}
	}

	suspend inline fun <reified Response> post(
		baseUrl: String,
		path: String,
		params: StringMap,
	): Response = httpClient.post("$baseUrl/$path") {
		this.body = FormDataContent(
			Parameters.build {
				params.forEach { append(it.key, it.value) }
			}
		)
	}

	suspend inline fun<Request : Any?, reified Response> post(
		baseUrl: String,
		path: String,
		noinline headers: HeadersBuilder.() -> Unit,
		request: Request,
	): Response = httpClient.post("$baseUrl/$path") {
		contentType(ContentType.Application.Json)
		headers(headers)
		if (request != null) body = request
	}

	suspend inline fun<reified Response> get(
		baseUrl: String,
		path: String,
		noinline headers: HeadersBuilder.() -> Unit,
		params: StringMap
	) : Response = httpClient.get("$baseUrl/$path"){
		headers(headers)
		params.forEach { parameter(it.key, it.value) }
	}
}