package kg.optima.mobile.core.common

import com.soywiz.krypto.sha256
import io.ktor.utils.io.core.*

object CryptographyUtils {
	fun getHash(input: String): String {
		val hashedBytes = input.toByteArray().sha256()
		return hashedBytes.base64
	}
}