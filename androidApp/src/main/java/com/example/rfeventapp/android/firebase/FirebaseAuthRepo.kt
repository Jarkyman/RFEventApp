package com.example.rfeventapp.android.firebase

import android.content.ContentValues
import android.util.Log
import com.example.rfeventapp.domain.User
import com.example.rfeventapp.usecase.loggedInUser
import com.example.rfeventapp.utils.*
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepo {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val database = Firebase.database

    val userRef = database.getReference(KEY_USERS)

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun logoutUser() {
        firebaseAuth.signOut()
    }

    suspend fun loginUser(email: String, pass: String): Pair<Boolean, String> {
        var result = Pair(false, "Fejl")
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        result = Pair(true, "Sucess")
                    } else {
                        println(it.exception.toString())
                        result = Pair(false, it.exception.toString())
                    }
                }.await()
            } catch (e: Exception) {
                result = Pair(false, e.localizedMessage.toString())
            }

        } else {
            result = Pair(false, "Skriv email og kodeord")
        }
        if (result.first) {
            getUser()
        }
        return result
    }

    suspend fun createUser(user: User, pass: String, passConfirm: String): Pair<Boolean, String> {
        var result = Pair(false, "Fejl")
        if (pass.isNotEmpty() && passConfirm.isNotEmpty()) {
            if (pass == passConfirm) {
                try {
                    firebaseAuth.createUserWithEmailAndPassword(user.email, pass)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val newUser = User(
                                    id = firebaseAuth.currentUser!!.uid,
                                    firstName = user.firstName,
                                    lastName = user.lastName,
                                    email = user.email,
                                    birthday = user.birthday,
                                    campName = user.campName,
                                    createdAt = user.createdAt,
                                    updatedAt = user.updatedAt
                                )
                                val userName = UserProfileChangeRequest.Builder()
                                    .setDisplayName("${user.firstName} ${user.lastName}").build()
                                firebaseAuth.currentUser!!.updateProfile(userName)
                                userRef.child(firebaseAuth.currentUser!!.uid).setValue(newUser)
                                result = Pair(true, "Success")
                            } else {
                                result = Pair(false, it.exception.toString())
                            }
                        }.await()
                } catch (e: Exception) {
                    result = Pair(false, e.localizedMessage.toString())
                }

            } else {
                result = Pair(false, "Kodeord passer ikke")
            }
        } else {
            result = Pair(false, "Mangler at udfylde felter")
        }
        if (result.first) {
            getUser()
        }
        return result
    }

    suspend fun updateUser(
        user: User,
        emailChanged: Boolean,
        nameChange: Boolean
    ): Pair<Boolean, String> {
        var result = Pair(false, "Error")
        var tempError = false
        try {
            if (emailChanged) {
                val authUser = firebaseAuth.currentUser
                authUser!!.updateEmail(user.email).await() //TODO: Add credentials with popup login
            }
            if (nameChange) {
                val userName = UserProfileChangeRequest.Builder()
                    .setDisplayName("${user.firstName} ${user.lastName}").build()
                firebaseAuth.currentUser!!.updateProfile(userName).await()
            }

            val updates = mapOf(
                "firstName" to user.firstName,
                "lastName" to user.lastName,
                "email" to user.email,
                "campName" to user.campName,
                "birthday" to user.birthday,
                "updatedAt" to user.updatedAt
            )

            userRef.child(firebaseAuth.currentUser!!.uid).updateChildren(updates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result = Pair(true, "Success")
                        _setLoggedInUser(user)
                    } else {
                        result = Pair(false, task.exception?.localizedMessage?.toString()!!)
                        tempError = true
                    }
                }.await()

            //TODO: Await virker ikke på realtime db... (HACK)
            while (!result.first && !tempError) {
                delay(300)
            }
            return result
        } catch (e: Exception) {
            return Pair(false, e.localizedMessage.toString())
        }
    }

    suspend fun updatePassword(
        pass: String, passNew: String, passConfirm: String
    ): Pair<Boolean, String> {
        var result = Pair(false, "Fejl")
        if (pass.isNotEmpty() && passNew.isNotEmpty() && passConfirm.isNotEmpty()) {
            if (passNew == passConfirm) {
                try {
                    val credential = EmailAuthProvider.getCredential(loggedInUser!!.email, pass)
                    val authUser = firebaseAuth.currentUser!!
                    authUser.reauthenticate(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            authUser.updatePassword(passNew)
                            result = Pair(true, "Success")
                        } else {
                            result = Pair(false, it.exception.toString())
                        }
                    }.await()
                } catch (e: Exception) {
                    result = Pair(false, e.toString())
                }
            } else {
                result = Pair(false, "Kodeord passer ikke")
            }
        } else {
            result = Pair(false, "Tomme felter")
        }
        return result
    }

    suspend fun deleteUser(): Pair<Boolean, String> {
        var result = Pair(false, "Fejl")
        //TODO: Make delete user
        return result
    }

    suspend fun forgotPassword(email: String): Pair<Boolean, String> {
        var result = Pair(false, "Fejl")
        try {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    result = Pair(true, "E-mail Sendt")
                } else {
                    result = Pair(false, "Fejl")
                }
            }.await()
        } catch (e: Exception) {
            result = Pair(false, e.localizedMessage.toString())
        }

        return result
    }

    suspend fun getUser(): User? {
        var result: User? = null
        try {
            userRef.child(firebaseAuth.currentUser!!.uid).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = User(
                        id = it.result.child(KEY_USER_ID).value.toString(),
                        createdAt = it.result.child(KEY_USER_CREATED_AT).value.toString(),
                        updatedAt = it.result.child(KEY_USER_UPDATED_AT).value.toString(),
                        firstName = it.result.child(KEY_USER_FIRST_NAME).value.toString(),
                        lastName = it.result.child(KEY_USER_LAST_NAME).value.toString(),
                        email = it.result.child(KEY_USER_EMAIL).value.toString(),
                        campName = it.result.child(KEY_USER_CAMP_NAME).value.toString(),
                        birthday = it.result.child(KEY_USER_BIRTHDAY).value.toString(),
                    )
                    result = user
                    _setLoggedInUser(user)
                } else {
                    result = null
                }
            }.await()
        } catch (e: Exception) {
            logoutUser()
            println(e.localizedMessage?.toString())
        }
        return null
    }

    private fun _setLoggedInUser(user: User) {
        loggedInUser = user
    }
}