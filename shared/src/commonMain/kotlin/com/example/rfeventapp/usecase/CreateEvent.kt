package com.example.rfeventapp.usecase

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant


class CreateEvent {

    fun getTimestamp(day: Int, time: String): Long {
        val hour = time.split(":")[0].toInt()
        val min = time.split(":")[1].toInt()

        if (day == 0) {
            val date = LocalDateTime(
                2023, 6, 24, hour, min
            )
            return date.toInstant(TimeZone.of("GMT+2")).toEpochMilliseconds()

        } else if (day == 1) {
            val date = LocalDateTime(
                2023, 6, 25, hour, min
            )
            return date.toInstant(TimeZone.of("GMT+2")).toEpochMilliseconds()
        } else if (day == 2) {
            val date = LocalDateTime(
                2023, 6, 26, hour, min
            )
            return date.toInstant(TimeZone.of("GMT+2")).toEpochMilliseconds()
        } else if (day == 3) {
            val date = LocalDateTime(
                2023, 6, 27, hour, min
            )
            return date.toInstant(TimeZone.of("GMT+2")).toEpochMilliseconds()
        } else if (day == 4) {
            val date = LocalDateTime(
                2023, 6, 28, hour, min
            )
            return date.toInstant(TimeZone.of("GMT+2")).toEpochMilliseconds()
        } else if (day == 5) {
            val date = LocalDateTime(
                2023, 6, 29, hour, min
            )
            return date.toInstant(TimeZone.of("GMT+2")).toEpochMilliseconds()
        } else if (day == 6) {
            val date = LocalDateTime(
                2023, 6, 30, hour, min
            )
            return date.toInstant(TimeZone.of("GMT+2")).toEpochMilliseconds()
        } else if (day == 7) {
            val date = LocalDateTime(
                2023, 7, 1, hour, min
            )
            return date.toInstant(TimeZone.of("GMT+2")).toEpochMilliseconds()
        } else if (day == 8) {
            val date = LocalDateTime(
                2023, 7, 2, hour, min
            )
            return date.toInstant(TimeZone.of("GMT+2")).toEpochMilliseconds()
        } else {
            return 0
        }
    }
}