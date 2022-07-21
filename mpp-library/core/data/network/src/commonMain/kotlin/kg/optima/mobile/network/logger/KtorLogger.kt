package kg.optima.mobile.network.logger

import io.ktor.client.plugins.logging.*

/**
 * Log для Ktor запросов
 * можно фильтровать
 */
class KtorLogger : Logger {
    override fun log(message: String) {
        println(message) // TODO log with max one line symbol count
    }

    companion object {
        private const val TAG = "KTOR"
        private const val KTOR_LOG_MASK_REQUEST = "REQUEST"
        private const val KTOR_LOG_MASK_RESPONSE = "RESPONSE"
    }
}
