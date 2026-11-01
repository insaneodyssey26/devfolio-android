package com.masum.devfolio.navigation

import kotlinx.serialization.Serializable

@Serializable
data object DiscoverDestination

@Serializable
data object ProjectsDestination

@Serializable
data object SettingsDestination

@Serializable
data object AboutDestination

@Serializable
data class DetailDestination(val slug: String)

@Serializable
data class ProjectDetailDestination(val slug: String)
