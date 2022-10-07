package kg.optima.mobile.network.client

import io.ktor.client.engine.ios.*
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.Foundation.*
import platform.Security.*

internal data class TrustSelfSignedCertificate internal constructor(
    private val validateTrust: Boolean = true
) : ChallengeHandler {

    override fun invoke(
        session: NSURLSession,
        task: NSURLSessionTask,
        challenge: NSURLAuthenticationChallenge,
        completionHandler: (NSURLSessionAuthChallengeDisposition, NSURLCredential?) -> Unit
    ) {
        val hostname = challenge.protectionSpace.host

        val serverTrust = challenge.protectionSpace.serverTrust
        val result: SecTrustResultType = 0u

        memScoped {
            val nativeResult = alloc<SecTrustResultTypeVar>()
            nativeResult.value = result
            SecTrustEvaluate(serverTrust!!, nativeResult.ptr)
        }

        val serverCertificate = SecTrustGetCertificateAtIndex(serverTrust, 0)
        val serverCertificateData = SecCertificateCopyData(serverCertificate)
        val data = CFDataGetBytePtr(serverCertificateData)
        val size = CFDataGetLength(serverCertificateData)

        val cert1 = NSData.dataWithBytes(data, size.toULong())
        val pathToCert = NSBundle.mainBundle.pathForResource("myOwnCert", "cer")

        val localCertificate: NSData = NSData.dataWithContentsOfFile(pathToCert!!)!!

        if (localCertificate == cert1) {
            completionHandler(
                NSURLSessionAuthChallengeUseCredential,
                NSURLCredential.create(serverTrust)
            )
        } else {
            completionHandler(NSURLSessionAuthChallengeCancelAuthenticationChallenge, null)
        }
    }
}