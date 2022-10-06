package kg.optima.mobile.network.client

import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory


class TLSSocketFactory(private val internalSSLSocketFactory: SSLSocketFactory) : SSLSocketFactory() {
	override fun getDefaultCipherSuites(): Array<String> {
		return internalSSLSocketFactory.defaultCipherSuites
	}

	override fun getSupportedCipherSuites(): Array<String> {
		return internalSSLSocketFactory.supportedCipherSuites
	}

	@Throws(IOException::class)
	override fun createSocket(): Socket {
		return enableTLSOnSocket(internalSSLSocketFactory.createSocket())
	}

	@Throws(IOException::class)
	override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket {
		return enableTLSOnSocket(internalSSLSocketFactory.createSocket(s, host, port, autoClose))
	}

	@Throws(IOException::class)
	override fun createSocket(host: String, port: Int): Socket {
		return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port))
	}

	@Throws(IOException::class)
	override fun createSocket(
		host: String,
		port: Int,
		localHost: InetAddress,
		localPort: Int
	): Socket {
		return enableTLSOnSocket(
			internalSSLSocketFactory.createSocket(
				host,
				port,
				localHost,
				localPort
			)
		)
	}

	@Throws(IOException::class)
	override fun createSocket(host: InetAddress, port: Int): Socket {
		return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port))
	}

	@Throws(IOException::class)
	override fun createSocket(
		address: InetAddress,
		port: Int,
		localAddress: InetAddress,
		localPort: Int
	): Socket {
		return enableTLSOnSocket(
			internalSSLSocketFactory.createSocket(
				address,
				port,
				localAddress,
				localPort
			)
		)
	}

	companion object {
		private fun enableTLSOnSocket(socket: Socket): Socket {
			if (socket is SSLSocket /*&& isTLSServerEnabled((SSLSocket) socket)*/) { // skip the fix if server doesn't provide there TLS version
				socket.enabledProtocols = arrayOf("TLSv1.2")
			}
			return socket
		}
	}
}