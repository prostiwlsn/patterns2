package com.example.h_bankpro.data.network

import android.content.Context
import okio.Buffer
import java.security.MessageDigest
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class IdempotencyKeyManager(
    context: Context
) {
    private val prefs = context.getSharedPreferences("IdempotencyPrefsPro", Context.MODE_PRIVATE)
    private val keyStore = ConcurrentHashMap<String, String>()
    private val ttlMs = 5 * 60 * 1000L

    init {
        cleanupOldKeys()
    }

    fun getIdempotencyKey(request: okhttp3.Request): String {
        val requestId = generateRequestId(request)
        return keyStore.getOrPut(requestId) {
            prefs.getString(requestId, null)?.takeIf { isKeyValid(requestId) } ?: UUID.randomUUID()
                .toString().also { key ->
                prefs.edit()
                    .putString(requestId, key)
                    .putLong("${requestId}_timestamp", System.currentTimeMillis())
                    .apply()
            }
        }.also {
        }
    }

    fun clearKey(request: okhttp3.Request) {
        val requestId = generateRequestId(request)
        keyStore.remove(requestId)
        prefs.edit()
            .remove(requestId)
            .remove("${requestId}_timestamp")
            .apply()
    }

    private fun isKeyValid(requestId: String): Boolean {
        val timestamp = prefs.getLong("${requestId}_timestamp", 0)
        val isValid = System.currentTimeMillis() - timestamp < ttlMs
        return isValid
    }

    private fun cleanupOldKeys() {
        val editor = prefs.edit()
        prefs.all.forEach { (key, _) ->
            if (key.endsWith("_timestamp")) {
                val requestId = key.removeSuffix("_timestamp")
                if (!isKeyValid(requestId)) {
                    editor.remove(requestId)
                    editor.remove("${requestId}_timestamp")
                }
            }
        }
        editor.apply()
    }

    private fun generateRequestId(request: okhttp3.Request): String {
        val method = request.method
        val url = request.url.toString()
        val bodyString = request.body?.let { body ->
            try {
                val buffer = Buffer()
                body.writeTo(buffer)
                val bodyContent = buffer.readUtf8()
                bodyContent
            } catch (e: Exception) {
                ""
            }
        } ?: ""
        val rawId = "$method:$url:$bodyString"
        return rawId.sha256()
    }

    private fun String.sha256(): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(this.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}