package com.example.rfeventapp.android.ui.event

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

class MyEventsViewModel() : ViewModel() {
    private val database = Firebase.database
    val eventRef = database.getReference(KEY_EVENTS)

    private val _events = MutableStateFlow<List<Event>>(emptyList<Event>())
    val events = _events.asStateFlow()

    init {
        //TODO: Skal fikse placeringen på ingen events.
        loadMyEvents()
    }

    fun loadMyEvents() {
        val myEventsQuery = eventRef

        myEventsQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = mutableListOf<Event>()
                for (eventSnapshot in dataSnapshot.children) {
                    if (eventSnapshot.child(KEY_EVENT_USER_ID).value == loggedInUser!!.id) {
                        val event = Event(
                            id = eventSnapshot.key.toString(),
                            userId = eventSnapshot.child(KEY_EVENT_USER_ID).value.toString(),
                            title = eventSnapshot.child(KEY_EVENT_TITLE).value.toString(),
                            campName = eventSnapshot.child(KEY_EVENT_CAMP_NAME).value.toString(),
                            dayTime = eventSnapshot.child(KEY_EVENT_DAY_TIME).value.toString(),
                            description = eventSnapshot.child(KEY_EVENT_DESCRIPTION).value.toString(),
                            location = eventSnapshot.child(KEY_EVENT_LOCATION).value.toString(),
                            image = eventSnapshot.child(KEY_EVENT_IMAGE).value.toString(),
                            participant = (eventSnapshot.child(KEY_EVENT_PARTICIPANTS).value as Long).toInt(),
                            timestamp = eventSnapshot.child(KEY_EVENT_TIMESTAMP).value as Long,
                            createdAt = eventSnapshot.child(KEY_EVENT_CREATED_AT).value.toString(),
                            updatedAt = eventSnapshot.child(KEY_EVENT_UPDATED_AT).value.toString(),
                            facebookLink = eventSnapshot.child(KEY_EVENT_FACEBOOK_LINK).value.toString(),
                        )
                        result.add(event)
                    }
                }
                viewModelScope.launch {
                    _events.value = result.toList().sortedBy { it.timestamp }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadEvents:onCancelled", databaseError.toException())
                // ...
            }
        })
    }
}