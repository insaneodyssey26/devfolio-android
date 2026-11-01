package com.masum.devfolio

import com.masum.devfolio.model.HackathonSearchResponse
import com.masum.devfolio.model.ProjectSearchResponse
import com.masum.devfolio.model.toDomain
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DevfolioSerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Test
    fun parseHackathonSearchPayload() {
        val rawJson = """
        {
          "hits": {
            "total": { "value": 1 },
            "hits": [
              {
                "_source": {
                  "uuid": "test-uuid-1",
                  "name": "Testathon",
                  "slug": "testathon",
                  "tagline": "Build the future",
                  "desc": "A great hackathon",
                  "cover_img": "https://assets.devfolio.co/cover.png",
                  "starts_at": "2026-11-20T10:00:00Z",
                  "ends_at": "2026-11-22T18:00:00Z",
                  "is_online": true,
                  "city": "Bengaluru",
                  "state": "Karnataka",
                  "country": "India",
                  "participants_count": 500,
                  "hackathon_setting": {
                    "logo": "https://assets.devfolio.co/logo.png",
                    "primary_color": "#3770FF",
                    "site": "https://testathon.co"
                  },
                  "hackathon_faqs": [
                    {
                      "uuid": "faq-1",
                      "question": "Who can apply?",
                      "answer": "Anyone!"
                    }
                  ]
                }
              }
            ]
          }
        }
        """.trimIndent()

        val response = json.decodeFromString<HackathonSearchResponse>(rawJson)
        assertNotNull(response.hits)
        assertEquals(1, response.hits?.hits?.size)

        val hackathon = response.hits?.hits?.first()?.source?.toDomain()
        assertNotNull(hackathon)
        assertEquals("Testathon", hackathon.name)
        assertEquals("testathon", hackathon.slug)
        assertEquals("#3770FF", hackathon.primaryColor)
        assertTrue(hackathon.isOnline)
        assertEquals(1, hackathon.faqs.size)
        assertEquals("Who can apply?", hackathon.faqs.first().question)
    }

    @Test
    fun parseProjectSearchPayload() {
        val rawJson = """
        {
          "hits": {
            "total": { "value": 1 },
            "hits": [
              {
                "_source": {
                  "uuid": "proj-uuid-1",
                  "name": "SuperApp",
                  "slug": "super-app",
                  "tagline": "A super app for everything",
                  "links": "https://github.com/developer/superapp,https://demo.superapp.io",
                  "has_github_link": true,
                  "hashtags": [
                    {
                      "uuid": "tag-1",
                      "name": "Kotlin",
                      "verified": true
                    }
                  ],
                  "members": [
                    {
                      "uuid": "member-1",
                      "username": "hacker1",
                      "first_name": "Dev",
                      "last_name": "One"
                    }
                  ],
                  "hackathon": {
                    "name": "ETHIndia",
                    "slug": "ethindia",
                    "primary_color": "#1C274B"
                  }
                }
              }
            ]
          }
        }
        """.trimIndent()

        val response = json.decodeFromString<ProjectSearchResponse>(rawJson)
        assertNotNull(response.hits)
        assertEquals(1, response.hits?.hits?.size)

        val project = response.hits?.hits?.first()?.source?.toDomain()
        assertNotNull(project)
        assertEquals("SuperApp", project.name)
        assertEquals("https://github.com/developer/superapp", project.githubLink)
        assertEquals("ETHIndia", project.hackathonName)
        assertEquals(1, project.hashtags.size)
        assertEquals("Kotlin", project.hashtags.first().name)
    }
}
