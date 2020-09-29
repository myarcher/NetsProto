package com.nets.applibs.glides

import java.io.IOException
import java.io.InputStream
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

object HttpsUtil {
    fun getSslSocketFactory(): SSLParam? {
        return getSslSocketFactory(
            null as X509TrustManager?,
            null as InputStream?,
            null as String?
        )
    }

    fun getSslSocketFactory(
        trustManager: X509TrustManager?,
        bksFile: InputStream?,
        password: String?,
        vararg certificates: InputStream?
    ): SSLParam? {
        val sslParam = SSLParam()
        return try {
            val keyManagers =
                prepareKeyManager(bksFile, password)
            val trustManagers =
                prepareTrustManager(*certificates)
            val manager: X509TrustManager
            manager = trustManager
                ?: (trustManagers?.let { chooseTrustManager(it) } ?: UnSafeTrustManager)
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(
                keyManagers,
                arrayOf<TrustManager?>(manager),
                null as SecureRandom?
            )
            sslParam.sSLSocketFactory = sslContext.socketFactory
            sslParam.trustManager = manager
            sslParam
        } catch (var9: NoSuchAlgorithmException) {
            throw AssertionError(var9)
        } catch (var10: KeyManagementException) {
            throw AssertionError(var10)
        }
    }

    private fun prepareKeyManager(
        bksFile: InputStream?,
        password: String?
    ): Array<KeyManager>? {
        return try {
            if (bksFile != null && password != null) {
                val clientKeyStore =
                    KeyStore.getInstance("BKS")
                clientKeyStore.load(bksFile, password.toCharArray())
                val kmf =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
                kmf.init(clientKeyStore, password.toCharArray())
                kmf.keyManagers
            } else {
                null
            }
        } catch (var4: Exception) {
            null
        }
    }

    private fun prepareTrustManager(vararg certificates: InputStream?): Array<TrustManager>? {
        return if (certificates != null && certificates.isNotEmpty()) {
            try {
                val certificateFactory =
                    CertificateFactory.getInstance("X.509")
                val keyStore =
                    KeyStore.getInstance(KeyStore.getDefaultType())
                keyStore.load(null as KeyStore.LoadStoreParameter?)
                var index = 0
                val var4: Array<out InputStream?> = certificates
                val var5 = certificates.size
                for (var6 in 0 until var5) {
                    val certStream = var4[var6]
                    val certificateAlias = Integer.toString(index++)
                    val cert =
                        certificateFactory.generateCertificate(certStream)
                    keyStore.setCertificateEntry(certificateAlias, cert)
                    try {
                        certStream?.close()
                    } catch (var11: IOException) {
                    }
                }
                val tmf =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                tmf.init(keyStore)
                tmf.trustManagers
            } catch (var12: Exception) {
                null
            }
        } else {
            null
        }
    }

    private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager? {
        val var2 = trustManagers.size
        for (var3 in 0 until var2) {
            val trustManager = trustManagers[var3]
            if (trustManager is X509TrustManager) {
                return trustManager
            }
        }
        return null
    }

    class UnSafeHostnameVerifier : HostnameVerifier {
        override fun verify(
            hostname: String,
            session: SSLSession
        ): Boolean {
            return true
        }
    }

    var UnSafeTrustManager: X509TrustManager =
        object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        }

    class SSLParam {
        var sSLSocketFactory: SSLSocketFactory? = null
        var trustManager: X509TrustManager? = null
    }
}