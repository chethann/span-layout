package io.github.chethann.spanlayout

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform