package kg.optima.mobile.network.client.ext

import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import kg.optima.mobile.network.client.NetworkClient


suspend inline fun <reified Request : Any> NetworkClient.sendWebSocket(
    host: String,
    port: Int,
    path: String,
    httpMethod: HttpMethod,
    body: Request
) {
    httpClient.webSocket(host = host, port = port, path = path, method = httpMethod) {
        sendSerialized(body)
    }
}

suspend inline fun <reified Response : Any> NetworkClient.receiveWebSocket(
    host: String,
    port: Int,
    path: String,
    httpMethod: HttpMethod,
    crossinline receive: (Response) -> Unit
) {
    httpClient.webSocket(host = host, port = port, path = path, method = httpMethod) {
        val obj = receiveDeserialized<Response>()
        receive(obj)
    }
}