package com.example.rfeventapp.domain

import com.example.rfeventapp.utils.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

//@Serializable
data class Event(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val campName: String = "",
    val description: String = "",
    val image: String = "",
    val dayTime: String = "",
    val location: String = "",
    val participant: Int = 0,
    val timestamp: Long = 0,
    val createdAt: String = "",
    val updatedAt: String = "",
    val facebookLink: String = "",
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "title" to title,
            "campName" to campName,
            "description" to description,
            "dayTime" to dayTime,
            "location" to location,
            "image" to image,
            "participant" to participant,
            "timestamp" to timestamp,
            "createdAt" to createdAt,
            "updatedAt" to updatedAt,
            "facebookLink" to facebookLink
        )
    }

    fun toJson(): String {
        val json = buildJsonObject {
            put("id", id)
            put("userId", userId)
            put("title", title)
            put("campName", campName)
            put("description", description)
            put("image", image)
            put("dayTime", dayTime)
            put("location", location)
            put("participant", participant)
            put("timestamp", timestamp)
            put("createdAt", createdAt)
            put("updatedAt", updatedAt)
            put("facebookLink", facebookLink)
        }
        return json.toString()
    }

    companion object {
        fun fromJson(jsonString: String): Event {
            val json = Json.decodeFromString<JsonObject>(jsonString)
            return Event(
                id = json[KEY_EVENT_ID]?.jsonPrimitive?.content ?: "",
                userId = json[KEY_EVENT_USER_ID]?.jsonPrimitive?.content ?: "",
                title = json[KEY_EVENT_TITLE]?.jsonPrimitive?.content ?: "",
                campName = json[KEY_EVENT_CAMP_NAME]?.jsonPrimitive?.content ?: "",
                description = json[KEY_EVENT_DESCRIPTION]?.jsonPrimitive?.content ?: "",
                image = json[KEY_EVENT_IMAGE]?.jsonPrimitive?.content ?: "",
                dayTime = json[KEY_EVENT_DAY_TIME]?.jsonPrimitive?.content ?: "",
                location = json[KEY_EVENT_LOCATION]?.jsonPrimitive?.content ?: "",
                participant = json[KEY_EVENT_PARTICIPANTS]?.jsonPrimitive?.content?.toInt() ?: 0,
                timestamp = json[KEY_EVENT_TIMESTAMP]?.jsonPrimitive?.content?.toLong() ?: 0,
                createdAt = json[KEY_EVENT_CREATED_AT]?.jsonPrimitive?.content ?: "",
                updatedAt = json[KEY_EVENT_UPDATED_AT]?.jsonPrimitive?.content ?: "",
                facebookLink = json[KEY_EVENT_FACEBOOK_LINK]?.jsonPrimitive?.content ?: "",
            )
        }
    }
}