//package kg.optima.mobile.network.client.ext
//
////import io.ktor.client.request.*
////import io.ktor.client.request.forms.*
////import io.ktor.http.*
////import io.ktor.http.content.*
////import io.ktor.util.*
//import kotlinx.serialization.KSerializer
//import kotlinx.serialization.json.Json
//
//fun <T> HttpRequestBuilder.setBody(serializer: KSerializer<T>, body: T, json: Json) {
//    setBody(json.encodeToString(serializer, body))
//}
//
//@OptIn(InternalAPI::class)
//fun HttpRequestBuilder.setBody(body: String) {
//    this.body = TextContent(
//       body,
//       ContentType.Application.Json.withoutParameters() // TODO split by content type
//    )
//}
//
//@OptIn(InternalAPI::class)
//fun HttpRequestBuilder.setFormData(map: Map<String, String>) {
//    this.body = FormDataContent(
//        formData = Parameters.build {
//            map.forEach {
//                append(it.key, it.value)
//            }
//        }
//    )
//}
//
//fun HttpRequestBuilder.setQueryParams(params: Map<String, Any>) {
//    params.forEach {
//        parameter(it.key, it.value)
//    }
//}
//
//fun HttpRequestBuilder.setQueryParams(params: List<Pair<String, Any>>) {
//    params.forEach {
//        parameter(it.first, it.second)
//    }
//}
//
//@OptIn(InternalAPI::class)
//fun HttpRequestBuilder.setMultiPartFormDataContent(formDataContent: MultiPartFormDataContent) {
//    this.body = formDataContent
//}