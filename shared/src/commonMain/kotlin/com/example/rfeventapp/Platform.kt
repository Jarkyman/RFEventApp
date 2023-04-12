package com.example.rfeventapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform