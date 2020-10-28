package com.feup.cmov.acme_client.Utils

import android.security.KeyPairGeneratorSpec
import android.util.Base64.*
import android.util.Log
import com.feup.cmov.acme_client.AcmeApplication
import okio.Buffer
import org.mindrot.jbcrypt.BCrypt
import java.math.BigInteger
import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.security.auth.x500.X500Principal


class Security {
    companion object {
        fun generateRsaKeyPair(userName: String): KeyPair {
            val kpg = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")

            val start = Calendar.getInstance()
            val end = Calendar.getInstance()
            end.add(Calendar.YEAR, 1)

            val spec = KeyPairGeneratorSpec
                .Builder(AcmeApplication.getAppContext())
                .setAlias(userName)
                .setKeySize(2048)
                .setSubject(X500Principal("CN=AcmeExperience, O=AcmeExperience Inc., C=PT"))
                .setSerialNumber(BigInteger.ONE)
                .setStartDate(start.time).setEndDate(end.time)
                .build();

            kpg.initialize(spec)
            return kpg.generateKeyPair()
        }

        fun retrieveRsaPrivateKey(userName: String): PrivateKey {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            val entry = keyStore.getEntry(userName, null)
            val privateKey = (entry as KeyStore.PrivateKeyEntry).privateKey
            return privateKey
        }

        fun makeSignature(key: PrivateKey, buffer: Buffer): String {
             val signature = Signature.getInstance("SHA256withRSA").run {
                initSign(key)
                update(buffer.readByteArray())
                sign()
            }
            return encodeToString(signature, NO_WRAP)
        }

        fun getRSAKeyAsString(key: Key): String {
            return encodeToString(key.encoded, DEFAULT);
        }

        fun generateHashedPassword(pass: String): String {
            return BCrypt.hashpw(pass, BCrypt.gensalt())
        }

        fun isPasswordCorrect(
            clearTextPassword: String,
            hashedPass: String
        ): Boolean {
            return BCrypt.checkpw(clearTextPassword, hashedPass)
        }
    }
}