package kg.optima.mobile.network.client

import android.annotation.SuppressLint
import android.content.res.Resources
import kg.optima.mobile.network.R
import okhttp3.OkHttpClient
import java.io.IOException
import java.io.InputStream
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

object AndroidOkHttpClient {
	fun setO24Certificate(okHttpClient: OkHttpClient.Builder) {
		try {
			val trusted = KeyStore.getInstance("BKS")

			val inputStream: InputStream = Resources.getSystem().openRawResource(R.raw.keyatf24)
			trusted.load(inputStream, "testing".toCharArray())
			inputStream.close()
			val trustManagerFactory = TrustManagerFactory.getInstance(
				TrustManagerFactory.getDefaultAlgorithm()
			)
			trustManagerFactory.init(trusted)
			val trustManagers = trustManagerFactory.trustManagers
			check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
				("Unexpected default trust managers:" + Arrays.toString(trustManagers))
			}
			val trustManager = trustManagers[0] as X509TrustManager
			val sslContext = SSLContext.getInstance("TLS")
			sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
			val sslSocketFactory = sslContext.socketFactory
			okHttpClient.sslSocketFactory(sslSocketFactory, trustManager)
			okHttpClient.hostnameVerifier { _, _ -> true }
		} catch (e: KeyStoreException) {
			e.printStackTrace()
		} catch (e: NoSuchAlgorithmException) {
			e.printStackTrace()
		} catch (e: KeyManagementException) {
			e.printStackTrace()
		} catch (e: IOException) {
			e.printStackTrace()
		} catch (e: CertificateException) {
			e.printStackTrace()
		}
	}

	fun initUnsafeSSL(okhttpBuilder: OkHttpClient.Builder) {
		try {
			val trustAllCerts = arrayOf<TrustManager>(
				@SuppressLint("CustomX509TrustManager")
				object : X509TrustManager {
					override fun checkClientTrusted(
						chain: Array<X509Certificate>,
						authType: String
					) = Unit

					override fun checkServerTrusted(
						chain: Array<X509Certificate>,
						authType: String
					) = Unit

					override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
				}
			)
			val sslContext = SSLContext.getInstance("SSL")
			sslContext.init(null, trustAllCerts, SecureRandom())
			okhttpBuilder.sslSocketFactory(sslContext.socketFactory, systemDefaultTrustManager())
				.hostnameVerifier { _, _ -> true }
		} catch (e: Exception) {
			throw RuntimeException(e)
		}
	}

	private fun systemDefaultTrustManager(): X509TrustManager {
		return try {
			val trustManagerFactory =
				TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
			trustManagerFactory.init(null as KeyStore?)
			val trustManagers = trustManagerFactory.trustManagers
			check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
				"Unexpected default trust managers:" + Arrays.toString(trustManagers)
			}
			trustManagers[0] as X509TrustManager
		} catch (e: GeneralSecurityException) {
			throw AssertionError()
		}
	}
}