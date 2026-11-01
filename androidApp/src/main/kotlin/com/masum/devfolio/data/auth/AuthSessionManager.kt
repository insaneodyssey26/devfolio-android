package com.masum.devfolio.data.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.URLDecoder

data class AuthUser(
    val username: String,
    val uuid: String,
    val accessToken: String,
    val rawCookies: String
)

sealed interface AuthState {
    data object Unauthenticated : AuthState
    data class Authenticated(val user: AuthUser) : AuthState
}

class AuthSessionManager private constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("devfolio_auth", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }
    private val scope = CoroutineScope(Dispatchers.IO)

    private val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(json)
        }
    }

    private val _authState = MutableStateFlow<AuthState>(loadInitialState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val currentUser: AuthUser?
        get() = (_authState.value as? AuthState.Authenticated)?.user

    private fun loadInitialState(): AuthState {
        val token = prefs.getString(KEY_ACCESS_TOKEN, null)
        val username = prefs.getString(KEY_USERNAME, null)
        val uuid = prefs.getString(KEY_UUID, null)
        val rawCookies = prefs.getString(KEY_RAW_COOKIES, "") ?: ""

        return if (!token.isNullOrBlank() && !username.isNullOrBlank() && !uuid.isNullOrBlank()) {
            AuthState.Authenticated(
                AuthUser(
                    username = username,
                    uuid = uuid,
                    accessToken = token,
                    rawCookies = rawCookies
                )
            )
        } else {
            AuthState.Unauthenticated
        }
    }

    fun parseAndSaveCookies(cookieHeader: String, onComplete: ((Boolean) -> Unit)? = null) {
        scope.launch {
            val result = parseCookiesSuspend(cookieHeader)
            withContext(Dispatchers.Main) {
                onComplete?.invoke(result)
            }
        }
    }

    private suspend fun parseCookiesSuspend(cookieHeader: String): Boolean {
        var token: String? = null
        var username: String? = null
        var uuid: String? = null

        val cookies = cookieHeader.split(";").map { it.trim() }
        for (cookie in cookies) {
            val parts = cookie.split("=", limit = 2)
            if (parts.size != 2) continue
            val name = parts[0].trim()
            val rawVal = parts[1].trim()

            when (name) {
                "access_token" -> {
                    token = rawVal
                }
                "devfolio_auth" -> {
                    runCatching {
                        val unquoted = URLDecoder.decode(rawVal, "UTF-8")
                        val payloadB64 = if (unquoted.startsWith("s:")) {
                            unquoted.substring(2).split(".")[0]
                        } else {
                            unquoted.split(".")[0]
                        }
                        val padded = payloadB64 + "=".repeat((-payloadB64.length) and 3)
                        val decodedBytes = Base64.decode(padded, Base64.DEFAULT)
                        val decodedJson = String(decodedBytes, Charsets.UTF_8)
                        val parsedObj = json.parseToJsonElement(decodedJson).jsonObject
                        token = parsedObj["access_token"]?.jsonPrimitive?.content
                    }
                }
                "devfolio_user" -> {
                    runCatching {
                        val unquoted = URLDecoder.decode(rawVal, "UTF-8")
                        val padded = unquoted + "=".repeat((-unquoted.length) and 3)
                        val decodedBytes = Base64.decode(padded, Base64.DEFAULT)
                        val decodedJson = String(decodedBytes, Charsets.UTF_8)
                        val parsedObj = json.parseToJsonElement(decodedJson).jsonObject
                        uuid = parsedObj["uuid"]?.jsonPrimitive?.content
                        username = parsedObj["username"]?.jsonPrimitive?.content
                    }
                }
            }
        }

        if (token.isNullOrBlank() || uuid.isNullOrBlank()) {
            return false
        }

        if (username.isNullOrBlank()) {
            username = fetchUsernameByUuid(uuid, token)
        }

        if (!username.isNullOrBlank()) {
            prefs.edit()
                .putString(KEY_ACCESS_TOKEN, token)
                .putString(KEY_USERNAME, username)
                .putString(KEY_UUID, uuid)
                .putString(KEY_RAW_COOKIES, cookieHeader)
                .apply()

            _authState.value = AuthState.Authenticated(
                AuthUser(
                    username = username,
                    uuid = uuid,
                    accessToken = token,
                    rawCookies = cookieHeader
                )
            )
            return true
        }

        return false
    }

    private suspend fun fetchUsernameByUuid(uuid: String, token: String): String? {
        return runCatching {
            val response = httpClient.get("https://api.devfolio.co/api/users/$uuid") {
                header("Authorization", token)
                header("access_token", token)
                header("User-Agent", "DevfolioMobileClient/1.0")
            }
            val text = response.bodyAsText()
            val parsedObj = json.parseToJsonElement(text).jsonObject
            parsedObj["username"]?.jsonPrimitive?.content
        }.getOrNull()
    }

    fun signOut() {
        prefs.edit().clear().apply()
        _authState.value = AuthState.Unauthenticated
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USERNAME = "username"
        private const val KEY_UUID = "uuid"
        private const val KEY_RAW_COOKIES = "raw_cookies"

        @Volatile
        private var instance: AuthSessionManager? = null

        fun getInstance(context: Context): AuthSessionManager {
            return instance ?: synchronized(this) {
                instance ?: AuthSessionManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
