package com.masum.devfolio.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DevfolioUserProfile(
    val uuid: String,
    val username: String,
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("last_name")
    val lastName: String? = null,
    val bio: String? = null,
    @SerialName("short_bio")
    val shortBio: String? = null,
    @SerialName("profile_image")
    val profileImage: String? = null,
    val address: ProfileAddress? = null,
    val skills: List<UserSkillWrapper> = emptyList(),
    val profiles: List<SocialProfileEntry> = emptyList(),
    val stats: DevfolioUserStats = DevfolioUserStats()
) {
    val displayName: String
        get() {
            val full = listOfNotNull(firstName, lastName).joinToString(" ").trim()
            return full.ifEmpty { username }
        }
}

@Serializable
data class ProfileAddress(
    val city: String? = null,
    val country: String? = null
) {
    val formatted: String?
        get() = listOfNotNull(city, country).filter { it.isNotBlank() }.joinToString(", ").ifEmpty { null }
}

@Serializable
data class UserSkillWrapper(
    val skill: SkillDetail? = null
)

@Serializable
data class SkillDetail(
    val uuid: String? = null,
    val name: String,
    val logo: String? = null,
    val status: String? = null
)

@Serializable
data class SocialProfileEntry(
    val uuid: String? = null,
    val value: String,
    val profile: SocialPlatformDetail? = null
)

@Serializable
data class SocialPlatformDetail(
    val uuid: String? = null,
    val name: String
)

@Serializable
data class DevfolioUserStats(
    @SerialName("hackathons_attended")
    val hackathonsAttended: Int = 0,
    @SerialName("hackathons_organized")
    val hackathonsOrganized: Int = 0,
    @SerialName("projects_built")
    val projectsBuilt: Int = 0,
    @SerialName("prizes_won")
    val prizesWon: Int = 0,
    @SerialName("cheer_received")
    val cheerReceived: Int = 0,
    @SerialName("cheer_given")
    val cheerGiven: Int = 0
)
