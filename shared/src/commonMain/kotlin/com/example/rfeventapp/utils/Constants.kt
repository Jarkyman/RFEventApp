package com.example.rfeventapp.utils

const val KEY_USERS = "users"
const val KEY_EVENTS = "events"
const val KEY_USERS_EVENTS = "users/events"
const val KEY_IMAGE_EVENTS = "images/events"

const val KEY_USER_ID = "id"
const val KEY_USER_FIRST_NAME = "firstName"
const val KEY_USER_LAST_NAME = "lastName"
const val KEY_USER_BIRTHDAY = "birthday"
const val KEY_USER_EMAIL = "email"
const val KEY_USER_CAMP_NAME = "campName"
const val KEY_USER_CREATED_AT = "createdAt"
const val KEY_USER_UPDATED_AT = "updatedAt"

const val KEY_EVENT_ID = "id"
const val KEY_EVENT_USER_ID = "userId"
const val KEY_EVENT_TITLE = "title"
const val KEY_EVENT_CAMP_NAME = "campName"
const val KEY_EVENT_DESCRIPTION = "description"
const val KEY_EVENT_DAY_TIME = "dayTime"
const val KEY_EVENT_LOCATION = "location"
const val KEY_EVENT_IMAGE = "image"
const val KEY_EVENT_PARTICIPANTS = "participant"
const val KEY_EVENT_TIMESTAMP = "timestamp"
const val KEY_EVENT_CREATED_AT = "createdAt"
const val KEY_EVENT_UPDATED_AT = "updatedAt"
const val KEY_EVENT_FACEBOOK_LINK = "facebookLink"

const val MAX_DESCRIPTION_LETTERS: Int = 1200
const val MAX_INPUT_LETTERS: Int = 20
const val MAX_LOCATION_LETTERS: Int = 5

const val IMAGE_URL_BASE = "https://firebasestorage.googleapis.com/v0/b/rf-app-6c0f6.appspot.com/o/images%2Fevents%2F"
const val IMAGE_URL_MEDIA = "?alt=media&token="

val DATE_LIST = listOf<String>(
    "LØR",
    "SØN",
    "MAN",
    "TIR",
    "ONS",
    "TOR",
    "FRE",
    "LØR",
    "SØN",
)
