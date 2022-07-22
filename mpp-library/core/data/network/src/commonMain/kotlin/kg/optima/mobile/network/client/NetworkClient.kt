package kg.optima.mobile.network.client

//import io.ktor.client.*
//import io.ktor.http.*
import kotlinx.serialization.KSerializer

interface NetworkClient {

//    val httpClient: HttpClient

    suspend fun <Request, Response> request(
        baseUrl: String,
        path: String,
        body: Request,
        serializer: KSerializer<Request>,
//        httpMethod: HttpMethod,
    ): Response?

    suspend fun <Request, Response> request(
        baseUrl: String,
        path: String,
        body: Request,
        serializer: KSerializer<Request>,
//        httpMethod: HttpMethod,
        defaultValue: Response,
    ): Response

}