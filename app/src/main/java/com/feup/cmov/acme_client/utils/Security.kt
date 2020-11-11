package com.feup.cmov.acme_client.utils

import android.security.KeyPairGeneratorSpec
import android.util.Base64.*
import com.feup.cmov.acme_client.AcmeApplication
import okio.Buffer
import org.mindrot.jbcrypt.BCrypt
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.security.auth.x500.X500Principal


/**
 * This object houses all the functions related to security.
 */
object Security {
    /**
     * Generates an RSA key pair and stores it in the Android Key store under the `username` alias.
     * @return The generated keypair.
     */
    fun generateRsaKeyPair(userName: String): KeyPair {
        val kpg = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")

        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 1)

        val spec = KeyPairGeneratorSpec
            .Builder(AcmeApplication.getAppContext())
            .setAlias(userName)
            .setKeySize(512)
            .setSubject(X500Principal("CN=AcmeExperience, O=AcmeExperience Inc., C=PT"))
            .setSerialNumber(BigInteger.ONE)
            .setStartDate(start.time).setEndDate(end.time)
            .build();

        kpg.initialize(spec)
        return kpg.generateKeyPair()
    }

    /**
     * Retrieves the private key with `username` alias from the Android key store.
     * @return The private key.
     */
    fun retrieveRsaPrivateKey(userName: String): PrivateKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val entry = keyStore.getEntry(userName, null)
        val privateKey = (entry as KeyStore.PrivateKeyEntry).privateKey
        return privateKey
    }

    /**
     * Signs `buffer` with `key` using the 'SHA256withRSA' algorithm.
     * @return The signature.
     */
    fun makeSignature(key: PrivateKey, buffer: Buffer): String {
        val signature = Signature.getInstance("SHA256withRSA").run {
            initSign(key)
            update(buffer.readByteArray())
            sign()
        }
        return encodeToString(signature, NO_WRAP)
    }

    /**
     * Encodes an RSA key as a base64 string.
     * @return The encoded RSA key.
     */
    fun getRSAKeyAsString(key: Key): String {
        return encodeToString(key.encoded, DEFAULT);
    }

    /**
     * Hashes the `password` using the BCrypt algorithm.
     * @return The hashed password.
     */
    fun generateHashedPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    /**
     * Verify if `clearTextPassword` hashed is equal to `hashedPass`.
     * @return The result of said validation.
     */
    fun isPasswordCorrect(
        clearTextPassword: String,
        hashedPass: String
    ): Boolean {
        return BCrypt.checkpw(clearTextPassword, hashedPass)
    }
}