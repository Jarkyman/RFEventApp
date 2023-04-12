package com.example.rfeventapp.android.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfeventapp.domain.Event
import com.example.rfeventapp.usecase.loggedInUser
import com.example.rfeventapp.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EventDetailViewModel(
    event: Event
) : ViewModel() {
    private val database = Firebase.database
    val eventRef = database.getReference(KEY_EVENTS)
    val userRef = database.getReference(KEY_USERS)

    private val _event = MutableStateFlow(event)
    val event = _event.asStateFlow()

    private val _isSubscribed = MutableStateFlow(false)
    val isSubscribed = _isSubscribed.asStateFlow()

    init {
        getEventFromDb(event.id)
        isUserSubscribed()
    }

    fun getEventFromDb(id: String) {
        val eventQuery = eventRef.child(id)
        eventQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val event = Event(
                    id = dataSnapshot.key.toString(),
                    userId = dataSnapshot.child(KEY_EVENT_USER_ID).value.toString(),
                    title = dataSnapshot.child(KEY_EVENT_TITLE).value.toString(),
                    campName = dataSnapshot.child(KEY_EVENT_CAMP_NAME).value.toString(),
                    dayTime = dataSnapshot.child(KEY_EVENT_DAY_TIME).value.toString(),
                    description = dataSnapshot.child(KEY_EVENT_DESCRIPTION).value.toString(),
                    location = dataSnapshot.child(KEY_EVENT_LOCATION).value.toString(),
                    image = dataSnapshot.child(KEY_EVENT_IMAGE).value.toString(),
                    participant = (dataSnapshot.child(KEY_EVENT_PARTICIPANTS).value as Long).toInt(),
                    timestamp = dataSnapshot.child(KEY_EVENT_TIMESTAMP).value as Long,
                    createdAt = dataSnapshot.child(KEY_EVENT_CREATED_AT).value.toString(),
                    updatedAt = dataSnapshot.child(KEY_EVENT_UPDATED_AT).value.toString(),
                    facebookLink = dataSnapshot.child(KEY_EVENT_FACEBOOK_LINK).value.toString(),
                )
                viewModelScope.launch {
                    _event.value = event

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                _event.value = Event()
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())

                // ...
            }
        })
    }

    fun isUserSubscribed() {
        try {
            viewModelScope.launch {
                val exist =
                    userRef.child(loggedInUser!!.id).child(KEY_EVENTS).child(_event.value.id).get()
                        .await()
                _isSubscribed.value = exist.exists()
            }
        } catch (e: Exception) {
            Log.w("TAG", "Is sub?", e)
        }
    }

    fun subscribeToEvent(isSub: Boolean) {
        var participants = _event.value.participant

        if (isSub) {
            participants = _event.value.participant + 1
            saveEventWithUser()
        } else {
            participants = _event.value.participant - 1
            removeEventWithUser()
        }
        eventRef.child(_event.value.id).child(KEY_EVENT_PARTICIPANTS).setValue(participants)
    }

    fun saveEventWithUser() {
        try {
            userRef.child(loggedInUser!!.id).child(KEY_EVENTS).child(_event.value.id)
                .setValue(_event.value)

        } catch (e: Exception) {
            Log.w("TAG", "saveEventWithUser:onCancelled", e)
        }
    }

    fun removeEventWithUser() {
        try {
            userRef.child(loggedInUser!!.id).child(KEY_EVENTS).child(_event.value.id).removeValue()

        } catch (e: Exception) {
            Log.w("TAG", "saveEventWithUser:onCancelled", e)
        }
    }

    fun subscribeToggle() {
        viewModelScope.launch {
            if (!_isSubscribed.value) {
                _isSubscribed.value = true
                subscribeToEvent(true)
            } else {
                _isSubscribed.value = false
                subscribeToEvent(false)
            }
        }
    }
}