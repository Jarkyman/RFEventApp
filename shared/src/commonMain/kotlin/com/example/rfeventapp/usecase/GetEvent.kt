package com.example.rfeventapp.usecase

import kotlinx.datetime.*

class GetEvent {

    fun getEventDate(timeStamp: Long): LocalDateTime {
        val instant = Instant.fromEpochMilliseconds(timeStamp)
        val date = instant.toLocalDateTime(kotlinx.datetime.TimeZone.of("GMT+2"))
        return date
        /*val eventDate = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(
                eventSnapshot.child(KEY_EVENT_TIMESTAMP).value as Long
            ), ZoneOffset.of("+02:00")*/
    }
}