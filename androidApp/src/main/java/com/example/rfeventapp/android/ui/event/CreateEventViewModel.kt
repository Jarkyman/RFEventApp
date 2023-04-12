package com.example.rfeventapp.android.ui.event

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rfeventapp.domain.Event
import com.example.rfeventapp.usecase.CreateEvent
import com.example.rfeventapp.usecase.loggedInUser
import com.example.rfeventapp.utils.*
import com.example.rfeventapp.utils.Result
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset

class CreateEventViewModel(
    val event: Event = Event()
) : ViewModel() {
    private val database = Firebase.database
    private val storage = Firebase.storage
    val storageRef = storage.getReference(KEY_IMAGE_EVENTS)
    val eventRef = database.getReference(KEY_EVENTS)
    val userRef = database.getReference(KEY_USERS)

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()
    private val _campName = MutableStateFlow(loggedInUser!!.campName)
    val campName = _campName.asStateFlow()
    private val _day = MutableStateFlow(0)
    val day = _day.asStateFlow()
    private val _time = MutableStateFlow(TextFieldValue("10:30"))
    val time = _time.asStateFlow()
    private val _category = MutableStateFlow("")
    val category = _category.asStateFlow()
    private val _location = MutableStateFlow("")
    val location = _location.asStateFlow()
    private val _image = MutableStateFlow<Bitmap?>(null)
    val image = _image.asStateFlow()
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri = _imageUri.asStateFlow()
    private val _facebookLink = MutableStateFlow("")
    val facebookLink = _facebookLink.asStateFlow()
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _dayDropdown = MutableStateFlow(false)
    val dayDropdown = _dayDropdown.asStateFlow()
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()
    private val _letters = MutableStateFlow(0)
    val letters = _letters.asStateFlow()

    init {
        if (event.timestamp != 0L) {
            _isEditMode.value = true
            updateAll(event)
        }
    }

    private fun updateAll(event: Event) {
        viewModelScope.launch {
            _title.value = event.title
            _campName.value = event.campName
            _day.value = DATE_LIST.indexOf(event.dayTime.uppercase().split(" ")[0])
            _time.value = TextFieldValue(event.dayTime.split(" ")[1])
            _location.value = event.location
            _facebookLink.value = event.facebookLink
            _description.value = event.description
            _letters.value = event.description.length

            _image.value = downloadBitmapFromUrl(
                "$IMAGE_URL_BASE${event.image.split("&")[0]}$IMAGE_URL_MEDIA${
                    event.image.split("&")[1]
                }"
            )
        }
        checkIfReady()
    }

    private fun downloadBitmapFromUrl(url: String): Bitmap? {
        println(url)
        var image: Bitmap? = null
        try {
            val uri =
                URL("http://www.undertoner.dk/wp-content/uploads/2022/06/Roskilde-Festival_Orange_Stage_credit_Flemming-Bo-Jensen-1200px.jpg")
            image = BitmapFactory.decodeStream(uri.openStream())
        } catch (e: Exception) {
            System.out.println(e);
        }
        return image
    }

    fun updateImageUri(uri: Uri) {
        viewModelScope.launch {
            _imageUri.value = uri
            checkIfReady()
        }
    }

    fun updateImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _image.value = bitmap
            checkIfReady()
        }
    }

    fun updateTitle(input: String) {
        viewModelScope.launch {
            if (Validation().checkString(input) == Result.Success && input.length <= MAX_INPUT_LETTERS) {
                _title.value = input
            }
            checkIfReady()
        }
    }

    fun updateCampName(input: String) {
        viewModelScope.launch {
            if (Validation().checkString(input) == Result.Success && input.length <= MAX_INPUT_LETTERS) {
                _campName.value = input
            }
            checkIfReady()
        }
    }

    fun updateDay(input: Int) {
        viewModelScope.launch {
            _day.value = input
            checkIfReady()
        }
    }

    fun updateTime(input: TextFieldValue) {
        viewModelScope.launch {
            _time.value = input
            checkIfReady()
        }
    }

    fun updateCategory(input: String) {
        //TODO: Add category
        viewModelScope.launch {
            _category.value = input
            checkIfReady()
        }
    }

    fun updateLocation(input: String) {
        viewModelScope.launch {
            println("${input.length} <= 5 = ${input.length <= MAX_LOCATION_LETTERS}")
            if (Validation().checkString(input) == Result.Success && input.length <= MAX_LOCATION_LETTERS) {
                _location.value = input
            }
            checkIfReady()
        }
    }

    fun updateFacebookLink(input: String) {
        viewModelScope.launch {
            //TODO: Add facebook link
            _facebookLink.value = input
            checkIfReady()
        }
    }

    fun updateDescription(input: String) {
        viewModelScope.launch {
            if (Validation().checkStringWithTap(input) == Result.Success && input.length <= MAX_DESCRIPTION_LETTERS) {
                _description.value = input
                _letters.value = _description.value.length
            }
            checkIfReady()
        }
    }

    fun toggleDayDropdown() {
        viewModelScope.launch {
            _dayDropdown.value = !_dayDropdown.value
        }
    }

    private fun checkIfReady() {
        if ((_imageUri.value != null || _isEditMode.value) && _title.value.isNotEmpty() && _campName.value.isNotEmpty() && _description.value.isNotEmpty()) {
            _isReady.value = true
        }
    }

    fun uploadImage(onBackClick: () -> Unit, context: Context) {
        viewModelScope.launch {
            _loading.value = true
        }
        val imgName =
            _title.value + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString()
        _imageUri.value?.let {
            val uploadWait = storageRef.child(imgName).putFile(it)
            uploadWait
                .addOnSuccessListener { result ->
                    result.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                        val imgUrl = imgName + "&" + url.toString().split("token=")[1]
                        println("UPLOAD ${imgUrl}")
                        createEvent(onBackClick, imgUrl, context)
                    }
                }.addOnCompleteListener { complete ->
                    if (!complete.isSuccessful) {
                        Toast.makeText(
                            context,
                            complete.exception?.localizedMessage.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        _loading.value = false
                    }
                }
        }
    }

    fun createEvent(onBackClick: () -> Unit, imgUrl: String, context: Context) {
        viewModelScope.launch {
            _loading.value = true
            if (_isReady.value) {
                val dayTime = Capitalizer().capitalizeFirstLetter(DATE_LIST[_day.value]) + " " + _time.value.text
                val timeStamp = CreateEvent().getTimestamp(_day.value, _time.value.text)
                try {
                    val eventPush = eventRef.push().key
                    if (eventPush != null) {
                        val event = Event(
                            id = eventPush,
                            userId = loggedInUser!!.id,
                            title = _title.value,
                            campName = _campName.value,
                            description = _description.value,
                            dayTime = dayTime,
                            location = _location.value,
                            image = imgUrl,
                            participant = 1,
                            timestamp = timeStamp,
                            createdAt = LocalDateTime.now().toString(),
                            updatedAt = LocalDateTime.now().toString(),
                            facebookLink = _facebookLink.value,
                        )
                        eventRef.child(eventPush).setValue(event)
                        userRef.child(loggedInUser!!.id).child(KEY_EVENTS).child(eventPush)
                            .setValue(event)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    onBackClick()
                                } else {
                                    Toast.makeText(
                                        context,
                                        it.exception?.localizedMessage?.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    _loading.value = false
                                }
                            }
                    } else {
                        Toast.makeText(context, "Fejl", Toast.LENGTH_SHORT).show()
                        _loading.value = false
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.localizedMessage?.toString() ?: "Fejl", Toast.LENGTH_LONG)
                        .show()
                    _loading.value = false
                }
            }
        }
    }

    fun saveEvent(onBackClick: () -> Unit, context: Context) {
        viewModelScope.launch {
            _loading.value = true
            if (_image.value != null) {
                val oldImg = event.image
                val imgName =
                    _title.value + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString()
                _imageUri.value?.let {
                    val uploadWait = storageRef.child(imgName).putFile(it)

                    uploadWait.addOnSuccessListener { result ->
                        result.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                            val imgUrl = imgName + "&" + url.toString().split("token=")[1]
                            val dayTime =
                                Capitalizer().capitalizeFirstLetter(DATE_LIST[_day.value]) + " " + _time.value.text
                            val timeStamp = CreateEvent().getTimestamp(_day.value, _time.value.text)
                            val newEvent = Event(
                                id = event.id,
                                userId = loggedInUser!!.id,
                                title = _title.value,
                                campName = _campName.value,
                                description = _description.value,
                                dayTime = dayTime,
                                location = _location.value,
                                image = imgUrl,
                                participant = event.participant,
                                timestamp = timeStamp,
                                createdAt = event.createdAt,
                                updatedAt = LocalDateTime.now().toString(),
                                facebookLink = _facebookLink.value,
                            )
                            eventRef.child(event.id).setValue(newEvent)
                            userRef.child(loggedInUser!!.id).child(KEY_EVENTS).child(event.id)
                                .setValue(newEvent)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        storageRef.child(oldImg.split("&")[0]).delete()
                                        onBackClick()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            it.exception?.localizedMessage.toString(),
                                            Toast.LENGTH_LONG
                                        ).show()
                                        _loading.value = false
                                    }
                                }
                        }
                    }
                }
            }

            if (_isReady.value && _image.value == null) {
                val dayTime = Capitalizer().capitalizeFirstLetter(DATE_LIST[_day.value]) + " " + _time.value.text
                val timeStamp = CreateEvent().getTimestamp(_day.value, _time.value.text)
                val newEvent = Event(
                    id = event.id,
                    userId = loggedInUser!!.id,
                    title = _title.value,
                    campName = _campName.value,
                    description = _description.value,
                    dayTime = dayTime,
                    location = _location.value,
                    image = event.image,
                    participant = event.participant,
                    timestamp = timeStamp,
                    createdAt = event.createdAt,
                    updatedAt = LocalDateTime.now().toString(),
                    facebookLink = _facebookLink.value,
                )
                eventRef.child(event.id).setValue(newEvent)
                userRef.child(loggedInUser!!.id).child(KEY_EVENTS).child(event.id).setValue(newEvent)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            onBackClick()
                        } else {
                            Toast.makeText(
                                context,
                                it.exception?.localizedMessage.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            _loading.value = false
                        }
                    }
            }
        }
    }

    fun deleteEvent(onBackClick: () -> Unit, context: Context) {
        viewModelScope.launch {
            _loading.value = true
            eventRef.child(event.id).removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    storageRef.child(event.image.split("&")[0]).delete()
                    onBackClick()
                }else {
                    Toast.makeText(context, it.exception?.localizedMessage.toString(), Toast.LENGTH_LONG).show()
                    _loading.value = false
                }
            }
        }
    }

    /*private fun getTimestamp(day: Int, time: String): Long {
        val hour = time.split(":")[0].toInt()
        val min = time.split(":")[1].toInt()

        if (day == 0) {
            val date = LocalDateTime.of(
                2023, 6, 24, hour, min
            )
            return date.toEpochSecond(ZoneOffset.UTC)

        } else if (day == 1) {
            val date = LocalDateTime.of(
                2023, 6, 25, hour, min
            )
            return date.toEpochSecond(ZoneOffset.UTC)
        } else if (day == 2) {
            val date = LocalDateTime.of(
                2023, 6, 26, hour, min
            )
            return date.toEpochSecond(ZoneOffset.UTC)
        } else if (day == 3) {
            val date = LocalDateTime.of(
                2023, 6, 27, hour, min
            )
            return date.toEpochSecond(ZoneOffset.UTC)
        } else if (day == 4) {
            val date = LocalDateTime.of(
                2023, 6, 28, hour, min
            )
            return date.toEpochSecond(ZoneOffset.UTC)
        } else if (day == 5) {
            val date = LocalDateTime.of(
                2023, 6, 29, hour, min
            )
            return date.toEpochSecond(ZoneOffset.UTC)
        } else if (day == 6) {
            val date = LocalDateTime.of(
                2023, 6, 30, hour, min
            )
            return date.toEpochSecond(ZoneOffset.UTC)
        } else if (day == 7) {
            val date = LocalDateTime.of(
                2023, 6, 31, hour, min
            )
            return date.toEpochSecond(ZoneOffset.UTC)
        } else if (day == 8) {
            val date = LocalDateTime.of(
                2023, 7, 1, hour, min
            )
            return date.toEpochSecond(ZoneOffset.UTC)
        } else if (day == 9) {
            val date = LocalDateTime.of(
                2023, 7, 2, hour, min
            )
            return date.toEpochSecond(ZoneOffset.UTC)
        } else {
            return 0
        }
    }*/
}