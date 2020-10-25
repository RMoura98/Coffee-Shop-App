package com.feup.cmov.acme_client.Utils

import android.security.KeyPairGeneratorSpec
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import com.feup.cmov.acme_client.AcmeApplication
import org.mindrot.jbcrypt.BCrypt
import java.math.BigInteger
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
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
                .setSubject(X500Principal("CN=ACME, O=ACME Inc., C=PT"))
                .setSerialNumber(BigInteger.ONE)
                .setStartDate(start.time).setEndDate(end.time)
                .build();

            kpg.initialize(spec)
            return kpg.generateKeyPair()
        }

        fun getPublicKey(publicKey: PublicKey): String {
            val fact = KeyFactory.getInstance("DSA")
            val spec: X509EncodedKeySpec = fact.getKeySpec(
                publicKey,
                X509EncodedKeySpec::class.java
            )
            return encodeToString(spec.getEncoded(), DEFAULT);
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