package kg.optima.mobile.network.client

import io.ktor.client.*
import kotlinx.serialization.json.Json

class NetworkClientImpl(
	override val httpClient: HttpClient,
	override val json: Json,
) : NetworkClient()
