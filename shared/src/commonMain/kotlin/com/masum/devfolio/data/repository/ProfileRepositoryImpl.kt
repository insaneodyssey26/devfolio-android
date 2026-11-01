package com.masum.devfolio.data.repository

import com.masum.devfolio.data.model.DevfolioUserProfile
import com.masum.devfolio.data.model.DevfolioUserStats
import com.masum.devfolio.network.createDevfolioHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class ProfileRepositoryImpl(
    private val httpClient: HttpClient = createDevfolioHttpClient(),
    private val json: Json = Json { ignoreUnknownKeys = true; isLenient = true }
) : ProfileRepository {

    private var inMemorySavedUsername: String? = null

    override suspend fun getProfileByUsername(username: String): Result<DevfolioUserProfile> = runCatching {
        val cleanHandle = username.trim().removePrefix("@")
        val url = "https://devfolio.co/@$cleanHandle"
        val response = httpClient.get(url) {
            header(HttpHeaders.UserAgent, "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Mobile Safari/537.36")
            header(HttpHeaders.Accept, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
        }
        val responseText = response.bodyAsText()

        val nextDataMarker = "<script id=\"__NEXT_DATA__\" type=\"application/json\">"
        val startIndex = responseText.indexOf(nextDataMarker)
        if (startIndex == -1) {
            throw IllegalStateException("User profile not found for @$cleanHandle")
        }

        val jsonStart = startIndex + nextDataMarker.length
        val jsonEnd = responseText.indexOf("</script>", jsonStart)
        if (jsonEnd == -1) {
            throw IllegalStateException("Invalid profile payload for @$cleanHandle")
        }

        val jsonString = responseText.substring(jsonStart, jsonEnd)
        val rootElement = json.parseToJsonElement(jsonString) as? JsonObject
            ?: throw IllegalStateException("Expected JsonObject in profile payload")
        val props = rootElement["props"] as? JsonObject
            ?: throw IllegalStateException("Missing props in profile payload")
        val pageProps = props["pageProps"] as? JsonObject
            ?: throw IllegalStateException("Missing pageProps in profile payload")
        val dehydratedState = pageProps["dehydratedState"] as? JsonObject
            ?: throw IllegalStateException("Missing dehydratedState in profile payload")
        val queries = dehydratedState["queries"] as? JsonArray
            ?: throw IllegalStateException("Missing queries in profile payload")

        var userProfile: DevfolioUserProfile? = null
        var userStats = DevfolioUserStats()

        for (queryElement in queries) {
            val queryObj = queryElement as? JsonObject ?: continue
            val queryKey = queryObj["queryKey"] as? JsonArray ?: continue
            val state = queryObj["state"] as? JsonObject ?: continue
            val data = state["data"] as? JsonObject ?: continue

            val firstKey = queryKey.getOrNull(0)
            val keyName = if (firstKey is JsonPrimitive) {
                firstKey.content
            } else {
                firstKey?.toString()?.replace("\"", "")
            }

            if (keyName == "userPublicProfile") {
                val users = data["users"] as? JsonArray
                val firstUser = users?.getOrNull(0) as? JsonObject
                if (firstUser != null) {
                    userProfile = json.decodeFromJsonElement(DevfolioUserProfile.serializer(), firstUser)
                }
            } else if (keyName == "userDevfolioStats") {
                userStats = json.decodeFromJsonElement(DevfolioUserStats.serializer(), data)
            }
        }

        if (userProfile == null) {
            throw IllegalStateException("User @$cleanHandle has no public Devfolio profile")
        }

        userProfile.copy(stats = userStats)
    }

    override suspend fun getSavedUsername(): String? = inMemorySavedUsername

    override suspend fun saveUsername(username: String) {
        inMemorySavedUsername = username.trim().removePrefix("@")
    }

    override suspend fun clearUsername() {
        inMemorySavedUsername = null
    }
}
