package com.masum.devfolio.navigation

import kotlinx.serialization.Serializable

@Serializable
data object DiscoverDestination

@Serializable
data class DetailDestination(val slug: String)
