package com.masum.devfolio.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HackathonSearchRequest(
    @SerialName("type") val type: String = "application_open",
    @SerialName("web") val web: Boolean = true,
    @SerialName("query") val query: String? = null,
    @SerialName("from") val from: Int = 0,
    @SerialName("size") val size: Int = 20
)

@Serializable
data class HackathonSearchResponse(
    @SerialName("hits") val hits: HackathonSearchHits? = null
)

@Serializable
data class HackathonSearchHits(
    @SerialName("total") val total: TotalHits? = null,
    @SerialName("hits") val hits: List<HackathonHit> = emptyList()
)

@Serializable
data class TotalHits(
    @SerialName("value") val value: Int = 0
)

@Serializable
data class HackathonHit(
    @SerialName("_source") val source: HackathonDetailPayload? = null
)

@Serializable
data class HackathonDetailPayload(
    @SerialName("uuid") val uuid: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("slug") val slug: String = "",
    @SerialName("tagline") val tagline: String? = null,
    @SerialName("desc") val desc: String? = null,
    @SerialName("cover_img") val coverImg: String? = null,
    @SerialName("starts_at") val startsAt: String? = null,
    @SerialName("ends_at") val endsAt: String? = null,
    @SerialName("is_online") val isOnline: Boolean = false,
    @SerialName("city") val city: String? = null,
    @SerialName("state") val state: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("location") val location: String? = null,
    @SerialName("participants_count") val participantsCount: Int = 0,
    @SerialName("hackathon_setting") val setting: HackathonSettingPayload? = null,
    @SerialName("hackathon_faqs") val faqs: List<HackathonFaq> = emptyList(),
    @SerialName("participants_details") val participants: List<ParticipantDetail> = emptyList()
)

@Serializable
data class HackathonSettingPayload(
    @SerialName("logo") val logo: String? = null,
    @SerialName("primary_color") val primaryColor: String? = null,
    @SerialName("site") val site: String? = null,
    @SerialName("discord") val discord: String? = null,
    @SerialName("twitter") val twitter: String? = null,
    @SerialName("reg_starts_at") val regStartsAt: String? = null,
    @SerialName("reg_ends_at") val regEndsAt: String? = null,
    @SerialName("external_apply_url") val externalApplyUrl: String? = null
)

@Serializable
data class HackathonFaq(
    @SerialName("uuid") val uuid: String = "",
    @SerialName("question") val question: String = "",
    @SerialName("answer") val answer: String = ""
)

@Serializable
data class ParticipantDetail(
    @SerialName("uuid") val uuid: String = "",
    @SerialName("username") val username: String = "",
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("profile_image") val profileImage: String? = null
)

data class Hackathon(
    val uuid: String,
    val name: String,
    val slug: String,
    val tagline: String,
    val description: String,
    val coverImg: String?,
    val logoImg: String?,
    val startsAt: String?,
    val endsAt: String?,
    val regStartsAt: String?,
    val regEndsAt: String?,
    val isOnline: Boolean,
    val city: String?,
    val state: String?,
    val country: String?,
    val location: String?,
    val participantsCount: Int,
    val primaryColor: String?,
    val siteUrl: String?,
    val discordUrl: String?,
    val faqs: List<HackathonFaq>,
    val participants: List<ParticipantDetail>
)

fun HackathonDetailPayload.toDomain(): Hackathon = Hackathon(
    uuid = uuid,
    name = name,
    slug = slug,
    tagline = tagline.orEmpty(),
    description = desc.orEmpty(),
    coverImg = coverImg,
    logoImg = setting?.logo,
    startsAt = startsAt,
    endsAt = endsAt,
    regStartsAt = setting?.regStartsAt,
    regEndsAt = setting?.regEndsAt,
    isOnline = isOnline,
    city = city,
    state = state,
    country = country,
    location = location,
    participantsCount = participantsCount,
    primaryColor = setting?.primaryColor,
    siteUrl = setting?.site,
    discordUrl = setting?.discord,
    faqs = faqs,
    participants = participants
)
