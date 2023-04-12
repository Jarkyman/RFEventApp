package com.example.rfeventapp.android.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfeventapp.domain.Event
import com.example.rfeventapp.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class HomeViewModel() : ViewModel() {
    private val TAG = "HOME VIEW MODEL"
    private val database = Firebase.database
    val eventRef = database.getReference(KEY_EVENTS)

    private val _popularList = MutableStateFlow(listOf<Event>())
    val popularList = _popularList.asStateFlow()

    private val _upcomingList = MutableStateFlow(listOf<Event>())
    val upcomingList = _upcomingList.asStateFlow()

    /*private val _state = MutableStateFlow(ScreenState())
    val state = _state.asStateFlow()*/

    private val _firstLoad = MutableStateFlow(true)
    val firstLoad = _firstLoad.asStateFlow()

    /*private val paginator = DefaultPaginator(
        initialKey = _state.value.latestEvent,
        onLoadUpdated = {
            _state.value = _state.value.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            repository.loadUpcomingEvents()
        },
        getNextKey = {
            _state.value.latestEvent + 2
        },
        onError = {
            _state.value = state.value.copy(error = it.toString())
        },
        onSuccess = { items, newKey ->
            _state.value = state.value.copy(
                items = state.value.items + items,
                latestEvent = newKey,
                endReached = items.isEmpty()
            )
        }
    )*/

    init {
        viewModelScope.launch {
            loadPopularEvents()
            loadUpcomingEvents()
        }
        //loadNextItems()
        _firstLoad.value = false
    }

    /*fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }*/

    fun loadPopularEvents() {
        println("TIMESTAMP = " + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString())
        val popularEventsQuery = eventRef
            .orderByChild(KEY_EVENT_TIMESTAMP)
            .startAfter(LocalDateTime.now().minusMinutes(15).toEpochSecond(ZoneOffset.UTC).toDouble(), KEY_EVENT_TIMESTAMP)

        popularEventsQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = mutableListOf<Event>()
                for (eventSnapshot in dataSnapshot.children) {
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
                result.sortByDescending { it.participant }
                viewModelScope.launch {
                    _popularList.value = result.toList().take(5)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPopularEvents:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

    fun loadUpcomingEvents() {
        //TODO: Noget pagination
        println("TIMESTAMP = " + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString())
        val upcomingEventsQuery = eventRef.orderByChild(KEY_EVENT_TIMESTAMP)
            .startAfter(LocalDateTime.now().minusMinutes(15).toEpochSecond(ZoneOffset.UTC).toDouble(), KEY_EVENT_TIMESTAMP)
            .limitToLast(20)

        upcomingEventsQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = mutableListOf<Event>()
                for (eventSnapshot in dataSnapshot.children) {
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
                viewModelScope.launch {
                    _upcomingList.value = result
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadUpcomingEvents:onCancelled", databaseError.toException())
                // ...
            }
        })
    }
}

data class ScreenState(
    val isLoading: Boolean = false,
    val items: List<Event> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val latestEvent: String = ""
)