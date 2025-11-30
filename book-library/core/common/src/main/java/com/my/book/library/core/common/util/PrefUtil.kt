package com.my.book.library.core.common.util

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.google.gson.Gson
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object PrefUtil {

    const val MY_LIBRARY_INFO = "MY_LIBRARY_INFO"


    private const val KEY_ALIAS = "PREF_MANAGER_AES_KEY"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val AES_MODE = "AES/GCM/NoPadding"
    private const val GCM_TAG_LENGTH = 128
    private const val GCM_IV_LENGTH = 12


    private var fileName = ""
    private var sharedPreferences: SharedPreferences? = null

    // 암호화 설정 여부
    private val isStringEncDec = false
    private val isIntEncDec = false
    private val isFloatEncDec = false
    private val isJsonEncDec = false
    private val isBooleanEncDec = false
    private val isLongEncDec = false

    fun setFileName(context: Context, name: String) {
        fileName = name

        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    /**
     * 암호화 키 만들기
     *
     * @return
     */
    fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }

        keyStore.getKey(KEY_ALIAS, null)?.let {
            return it as SecretKey
        }

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true) // IV 자동 생성
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    /**
     * 암호화 진행
     *
     * @param value
     * @param secretKey
     * @return
     */
    fun encrypt(value: String, secretKey: SecretKey): String? {
        return try {
            val cipher = Cipher.getInstance(AES_MODE).apply {
                init(Cipher.ENCRYPT_MODE, secretKey)
            }
            val iv = cipher.iv
            val encrypted = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
            Base64.encodeToString(iv + encrypted, Base64.NO_WRAP)
        } catch (e: Exception) {
            LogUtil.e_dev("암호화 실패 $e")
            null
        }
    }

    /**
     * 복호화 진행
     *
     * @param value
     * @param secretKey
     * @return
     */
    fun decrypt(value: String, secretKey: SecretKey): String? {
        return try {
            val combined = Base64.decode(value, Base64.NO_WRAP)
            val iv = combined.copyOfRange(0, GCM_IV_LENGTH)
            val encrypted = combined.copyOfRange(GCM_IV_LENGTH, combined.size)
            val cipher = Cipher.getInstance(AES_MODE).apply {
                init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH, iv))
            }
            String(cipher.doFinal(encrypted), Charsets.UTF_8)
        } catch (e: Exception) {
            LogUtil.e_dev("복호화 실패 $e")
            null
        }
    }

    private fun putEncrypted(key: String, value: String, isEncrypt: Boolean): Boolean {
        if(isEncrypt) {
            val secretKey: SecretKey = getOrCreateSecretKey()
            val encrypted = encrypt(value, secretKey) ?: return false
            return sharedPreferences?.edit()?.putString(key, encrypted)?.commit() ?: false
        } else {
            return sharedPreferences?.edit()?.putString(key, value)?.commit() ?: false
        }
    }

    private fun getDecrypted(key: String, defaultValue: String = "", isDecrypt: Boolean): String {
        if(isDecrypt) {
            val secretKey: SecretKey = getOrCreateSecretKey()
            val encoded = sharedPreferences?.getString(key, null) ?: return defaultValue
            return decrypt(encoded, secretKey) ?: defaultValue
        } else {
            return sharedPreferences?.getString(key, null) ?: return defaultValue
        }
    }

    fun setString(key: String, value: String): Boolean {
        return putEncrypted(key, value, isEncrypt = isStringEncDec)
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return getDecrypted(key, defaultValue, isDecrypt = isStringEncDec)
    }

    fun setInt(key: String, value: Int): Boolean {
        return putEncrypted(key, value.toString(), isEncrypt = isIntEncDec)
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return getDecrypted(key, defaultValue.toString(), isDecrypt = isIntEncDec).toIntOrNull() ?: defaultValue
    }

    fun setBoolean(key: String, value: Boolean): Boolean {
        return putEncrypted(key, value.toString(), isEncrypt = isBooleanEncDec)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return getDecrypted(key, defaultValue.toString(), isDecrypt = isBooleanEncDec).toBoolean()
    }

    fun setLong(key: String, value: Long): Boolean {
        return putEncrypted(key, value.toString(), isEncrypt = isLongEncDec)
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return getDecrypted(key, defaultValue.toString(), isDecrypt = isLongEncDec).toLongOrNull() ?: defaultValue
    }

    fun setFloat(key: String, value: Float): Boolean {
        return putEncrypted(key, value.toString(), isEncrypt = isFloatEncDec)
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return getDecrypted(key, defaultValue.toString(), isDecrypt = isFloatEncDec).toFloatOrNull() ?: defaultValue
    }

    fun <T> setJson(key: String, obj: T): Boolean {
        return putEncrypted(key, Gson().toJson(obj), isEncrypt = isJsonEncDec)
    }

    fun <T> getJson(key: String, type: Class<T>): T? {
        val json = getDecrypted(key, "", isDecrypt = isJsonEncDec)
        return if (json.isNotEmpty()) {
            Gson().fromJson(json, type)
        } else {
            null
        }
    }

    fun removeKey(key: String): Boolean {
        return sharedPreferences?.edit()?.remove(key)?.commit() ?: false
    }

    fun allClear(): Boolean {
        return sharedPreferences?.edit()?.clear()?.commit() ?: false
    }

}