package com.example.rfeventapp.android.ui.calender

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
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class CalenderViewModel : ViewModel() {
    private val database = Firebase.database
    val eventRef = database.getReference(KEY_EVENTS)
    val userRef = database.getReference(KEY_USERS)

    private val _selectedDate = MutableStateFlow(0)
    val selectedDate = _selectedDate.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(emptyList<Event>())
    val events = _events.asStateFlow()

    val days = mapOf(
        0 to LocalDate.of(2023, 6, 24),
        1 to LocalDate.of(2023, 6, 25),
        2 to LocalDate.of(2023, 6, 26),
        3 to LocalDate.of(2023, 6, 27),
        4 to LocalDate.of(2023, 6, 28),
        5 to LocalDate.of(2023, 6, 29),
        6 to LocalDate.of(2023, 6, 30),
        7 to LocalDate.of(2023, 7, 1)
    )

    init {
        viewModelScope.launch {
            _selectedDate.value = getToday()
            getSubscribedEventsByDate(days[_selectedDate.value]!!)
        }
    }

    private fun getToday(): Int {
        var result = 0
        days.forEach {
            if (LocalDate.now().equals(it.value)) {
                result = it.key
            }
        }
        return result
    }

    fun updateSelectedDate(index: Int) {
        viewModelScope.launch {
            _selectedDate.value = index
            getSubscribedEvents()
        }
    }

    fun getSubscribedEvents() {
        if (_selectedDate.value == 0) {
            getSubscribedEventsByDate(days[0]!!)
        } else if (_selectedDate.value == 1) {
            getSubscribedEventsByDate(days[1]!!)
        } else if (_selectedDate.value == 2) {
            getSubscribedEventsByDate(days[2]!!)
        } else if (_selectedDate.value == 3) {
            getSubscribedEventsByDate(days[3]!!)
        } else if (_selectedDate.value == 4) {
            getSubscribedEventsByDate(days[4]!!)
        } else if (_selectedDate.value == 5) {
            getSubscribedEventsByDate(days[5]!!)
        } else if (_selectedDate.value == 6) {
            getSubscribedEventsByDate(days[6]!!)
        } else if (_selectedDate.value == 7) {
            getSubscribedEventsByDate(days[7]!!)
        } else {
            //TODO: Skift til første lørdag
        }
    }

    fun getSubscribedEventsByDate(date: LocalDate) {
        val userEventsQueries = userRef.child(loggedInUser!!.id).child(KEY_EVENTS)
        try {
            eventRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val allEventList = mutableListOf<Event>()
                    val result = mutableListOf<Event>()
                    for (eventSnapshot in dataSnapshot.children) {
                        val eventDate = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(
                                eventSnapshot.child(KEY_EVENT_TIMESTAMP).value as Long
                            ), ZoneOffset.of("+02:00")
                        )
                        if (eventDate.dayOfMonth == date.dayOfMonth) {
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
                            allEventList.add(event)
                        }
                    }
                    /*viewModelScope.launch {
                        try {
                            for (event in allEventList) {
                                userEventsQueries.child(event.id).get().addOnCompleteListener {
                                    println("Before success = ${event.id} / ${it.result.child(
                                        KEY_EVENT_ID).value}")
                                    if (it.isSuccessful) {
                                        if (event.id == it.result.child(KEY_EVENT_ID).value) {
                                            result.add(event)
                                            println("Event fundet")
                                        } else {
                                            println("REMOVE")
                                            //userEventsQueries.child(event.id).
                                        }
                                    } else {
                                        //
                                    }
                                }.await()
                            }
                        }catch (e: Exception) {
                            println(e)
                        }
                    }*/

                    userEventsQueries.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(userDataSnapshot: DataSnapshot) {
                            val result2 = mutableListOf<Event>()
                            for (eventSnapshot in userDataSnapshot.children) {
                                val eventDate = LocalDateTime.ofInstant(
                                    Instant.ofEpochMilli(
                                        eventSnapshot.child(KEY_EVENT_TIMESTAMP).value as Long
                                    ), ZoneOffset.of("+02:00")
                                )
                                if (eventDate.dayOfMonth == date.dayOfMonth) {
                                    val eventUser = Event(
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
                                    for (rEvent in allEventList) {
                                        println(
                                            "Before success = ${rEvent.id} / ${eventSnapshot.key.toString()}"
                                        )
                                        if (rEvent.id == eventSnapshot.key.toString()) {
                                            result2.add(rEvent)
                                            println("Event fundet")
                                        } else {
                                            //TODO: Remove event...
                                            //dataSnapshot.ref.removeValue()
                                        }
                                    }
                                }
                            }
                            viewModelScope.launch {
                                _events.value = result2.toList().sortedBy { it.timestamp }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w("TAG", "loadEvents:onCancelled", databaseError.toException())
                            // ...
                        }
                    })
                    //_events.value = result.toList().sortedBy { it.timestamp }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("TAG", "loadEvents:onCancelled", databaseError.toException())
                    // ...
                }
            })
        }catch (e: Exception) {
            println(e.localizedMessage)
        }

    }

}