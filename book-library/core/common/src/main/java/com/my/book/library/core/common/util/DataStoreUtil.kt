package com.my.book.library.core.common.util

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * DataStore 기반의 암호화된 데이터 저장소 유틸리티
 * PrefUtil과 유사한 인터페이스를 제공하되, DataStore를 사용하여 더 안전하고 비동기적인 데이터 저장을 지원합니다.
 */
class DataStoreUtil {

    companion object {
        const val MY_LIBRARY_INFO = "MY_LIBRARY_INFO"
    }

    private val KEY_ALIAS = "STORE_MANAGER_AES_KEY"
    private val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val AES_MODE = "AES/GCM/NoPadding"
    private val GCM_TAG_LENGTH = 128
    private val GCM_IV_LENGTH = 12

    private var storeName = ""

    // DataStore 인스턴스를 Context의 확장 프로퍼티로 생성
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "encrypted_datastore")
    private var dataStore: DataStore<Preferences>? = null

    // 암호화 설정 여부
    private val isStringEncDec = false
    private val isIntEncDec = false
    private val isFloatEncDec = false
    private val isJsonEncDec = false
    private val isBooleanEncDec = false
    private val isLongEncDec = false

    /**
     * DataStore 초기화
     *
     * @param context Application Context
     * @param name DataStore 파일 이름 (기본값: "encrypted_datastore")
     */
    fun initialize(context: Context, name: String = "encrypted_datastore") {
        storeName = name
        dataStore = context.dataStore
    }

    /**
     * 암호화 키 생성 또는 가져오기
     *
     * @return SecretKey
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
     * @param value 암호화할 문자열
     * @param secretKey 암호화에 사용할 SecretKey
     * @return 암호화된 문자열 (Base64 인코딩)
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
     * @param value 복호화할 문자열 (Base64 인코딩된 상태)
     * @param secretKey 복호화에 사용할 SecretKey
     * @return 복호화된 문자열
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

    /**
     * 암호화된 값을 DataStore에 저장
     */
    private suspend fun putEncrypted(key: String, value: String, isEncrypt: Boolean) {
        val prefKey = stringPreferencesKey(key)
        val finalValue = if (isEncrypt) {
            val secretKey: SecretKey = getOrCreateSecretKey()
            encrypt(value, secretKey) ?: value
        } else {
            value
        }

        dataStore?.edit { preferences ->
            preferences[prefKey] = finalValue
        }
    }

    /**
     * DataStore에서 암호화된 값을 가져와 복호화
     */
    private suspend fun getDecrypted(key: String, defaultValue: String = "", isDecrypt: Boolean): String {
        val prefKey = stringPreferencesKey(key)
        val value = dataStore?.data?.map { preferences ->
            preferences[prefKey] ?: defaultValue
        }?.first() ?: defaultValue

        return if (isDecrypt && value.isNotEmpty() && value != defaultValue) {
            val secretKey: SecretKey = getOrCreateSecretKey()
            decrypt(value, secretKey) ?: defaultValue
        } else {
            value
        }
    }

    // String 저장/가져오기
    suspend fun setString(key: String, value: String): Boolean {
        return try {
            putEncrypted(key, value, isEncrypt = isStringEncDec)
            true
        } catch (e: Exception) {
            LogUtil.e_dev("setString 실패: $e")
            false
        }
    }

    suspend fun getString(key: String, defaultValue: String = ""): String {
        return getDecrypted(key, defaultValue, isDecrypt = isStringEncDec)
    }

    fun getStringFlow(key: String, defaultValue: String = ""): Flow<String> {
        val prefKey = stringPreferencesKey(key)
        return dataStore?.data?.map { preferences ->
            val value = preferences[prefKey] ?: defaultValue
            if (isStringEncDec && value.isNotEmpty() && value != defaultValue) {
                val secretKey: SecretKey = getOrCreateSecretKey()
                decrypt(value, secretKey) ?: defaultValue
            } else {
                value
            }
        } ?: kotlinx.coroutines.flow.flowOf(defaultValue)
    }

    // Int 저장/가져오기
    suspend fun setInt(key: String, value: Int): Boolean {
        return try {
            putEncrypted(key, value.toString(), isEncrypt = isIntEncDec)
            true
        } catch (e: Exception) {
            LogUtil.e_dev("setInt 실패: $e")
            false
        }
    }

    suspend fun getInt(key: String, defaultValue: Int = 0): Int {
        return getDecrypted(key, defaultValue.toString(), isDecrypt = isIntEncDec).toIntOrNull() ?: defaultValue
    }

    fun getIntFlow(key: String, defaultValue: Int = 0): Flow<Int> {
        val prefKey = stringPreferencesKey(key)
        return dataStore?.data?.map { preferences ->
            val value = preferences[prefKey] ?: defaultValue.toString()
            if (isIntEncDec && value.isNotEmpty()) {
                val secretKey: SecretKey = getOrCreateSecretKey()
                decrypt(value, secretKey)?.toIntOrNull() ?: defaultValue
            } else {
                value.toIntOrNull() ?: defaultValue
            }
        } ?: kotlinx.coroutines.flow.flowOf(defaultValue)
    }

    // Boolean 저장/가져오기
    suspend fun setBoolean(key: String, value: Boolean): Boolean {
        return try {
            putEncrypted(key, value.toString(), isEncrypt = isBooleanEncDec)
            true
        } catch (e: Exception) {
            LogUtil.e_dev("setBoolean 실패: $e")
            false
        }
    }

    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return getDecrypted(key, defaultValue.toString(), isDecrypt = isBooleanEncDec).toBoolean()
    }

    fun getBooleanFlow(key: String, defaultValue: Boolean = false): Flow<Boolean> {
        val prefKey = stringPreferencesKey(key)
        return dataStore?.data?.map { preferences ->
            val value = preferences[prefKey] ?: defaultValue.toString()
            if (isBooleanEncDec && value.isNotEmpty()) {
                val secretKey: SecretKey = getOrCreateSecretKey()
                decrypt(value, secretKey)?.toBoolean() ?: defaultValue
            } else {
                value.toBoolean()
            }
        } ?: kotlinx.coroutines.flow.flowOf(defaultValue)
    }

    // Long 저장/가져오기
    suspend fun setLong(key: String, value: Long): Boolean {
        return try {
            putEncrypted(key, value.toString(), isEncrypt = isLongEncDec)
            true
        } catch (e: Exception) {
            LogUtil.e_dev("setLong 실패: $e")
            false
        }
    }

    suspend fun getLong(key: String, defaultValue: Long = 0L): Long {
        return getDecrypted(key, defaultValue.toString(), isDecrypt = isLongEncDec).toLongOrNull() ?: defaultValue
    }

    fun getLongFlow(key: String, defaultValue: Long = 0L): Flow<Long> {
        val prefKey = stringPreferencesKey(key)
        return dataStore?.data?.map { preferences ->
            val value = preferences[prefKey] ?: defaultValue.toString()
            if (isLongEncDec && value.isNotEmpty()) {
                val secretKey: SecretKey = getOrCreateSecretKey()
                decrypt(value, secretKey)?.toLongOrNull() ?: defaultValue
            } else {
                value.toLongOrNull() ?: defaultValue
            }
        } ?: kotlinx.coroutines.flow.flowOf(defaultValue)
    }

    // Float 저장/가져오기
    suspend fun setFloat(key: String, value: Float): Boolean {
        return try {
            putEncrypted(key, value.toString(), isEncrypt = isFloatEncDec)
            true
        } catch (e: Exception) {
            LogUtil.e_dev("setFloat 실패: $e")
            false
        }
    }

    suspend fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return getDecrypted(key, defaultValue.toString(), isDecrypt = isFloatEncDec).toFloatOrNull() ?: defaultValue
    }

    fun getFloatFlow(key: String, defaultValue: Float = 0f): Flow<Float> {
        val prefKey = stringPreferencesKey(key)
        return dataStore?.data?.map { preferences ->
            val value = preferences[prefKey] ?: defaultValue.toString()
            if (isFloatEncDec && value.isNotEmpty()) {
                val secretKey: SecretKey = getOrCreateSecretKey()
                decrypt(value, secretKey)?.toFloatOrNull() ?: defaultValue
            } else {
                value.toFloatOrNull() ?: defaultValue
            }
        } ?: kotlinx.coroutines.flow.flowOf(defaultValue)
    }

    // JSON 저장/가져오기
    suspend fun <T> setJson(key: String, obj: T): Boolean {
        return try {
            putEncrypted(key, Gson().toJson(obj), isEncrypt = isJsonEncDec)
            true
        } catch (e: Exception) {
            LogUtil.e_dev("setJson 실패: $e")
            false
        }
    }

    suspend fun <T> getJson(key: String, type: Class<T>): T? {
        val json = getDecrypted(key, "", isDecrypt = isJsonEncDec)
        return if (json.isNotEmpty()) {
            try {
                Gson().fromJson(json, type)
            } catch (e: Exception) {
                LogUtil.e_dev("JSON 파싱 실패 $e")
                null
            }
        } else {
            null
        }
    }

    fun <T> getJsonFlow(key: String, type: Class<T>): Flow<T?> {
        val prefKey = stringPreferencesKey(key)
        return dataStore?.data?.map { preferences ->
            val value = preferences[prefKey] ?: ""
            if (value.isNotEmpty()) {
                val json = if (isJsonEncDec) {
                    val secretKey: SecretKey = getOrCreateSecretKey()
                    decrypt(value, secretKey) ?: ""
                } else {
                    value
                }
                try {
                    Gson().fromJson(json, type)
                } catch (e: Exception) {
                    LogUtil.e_dev("JSON 파싱 실패 $e")
                    null
                }
            } else {
                null
            }
        } ?: kotlinx.coroutines.flow.flowOf(null)
    }

    // 특정 키 삭제
    suspend fun removeKey(key: String) {
        val prefKey = stringPreferencesKey(key)
        dataStore?.edit { preferences ->
            preferences.remove(prefKey)
        }
    }

    // 모든 데이터 삭제
    suspend fun allClear() {
        dataStore?.edit { preferences ->
            preferences.clear()
        }
    }
}