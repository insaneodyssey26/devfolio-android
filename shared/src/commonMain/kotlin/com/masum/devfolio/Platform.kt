package com.masum.devfolio

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform