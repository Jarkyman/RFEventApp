package com.example.rfeventapp.domain

import com.example.rfeventapp.utils.*

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val birthday: String = "",
    val campName: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            KEY_USER_ID to id,
            KEY_USER_FIRST_NAME to firstName,
            KEY_USER_LAST_NAME to lastName,
            KEY_USER_EMAIL to email,
            KEY_USER_BIRTHDAY to birthday,
            KEY_USER_CAMP_NAME to campName,
            KEY_USER_CREATED_AT to createdAt,
            KEY_USER_UPDATED_AT to updatedAt
        )
    }
}