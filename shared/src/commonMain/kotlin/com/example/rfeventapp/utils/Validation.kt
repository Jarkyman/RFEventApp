package com.example.rfeventapp.utils

sealed class Result {
    object Success : Result()
    class Error(val message: String) : Result()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success"
            is Error -> message
        }
    }
}
class Validation() {
    fun checkString(input: String): Result {
        val pattern = Regex("[\\p{L}0-9 ]+" )
        return if (input.matches(pattern) || input == "") {
            Result.Success
        }else {
            Result.Error("Input contains invalid characters")
        }
    }

    // # / ?
    fun checkStringWithTap(input: String): Result {
        /*val pattern = Regex("[\\p{L}0-9\\s\\p{So}.,\"\'%()&]+")
        return if (input.matches(pattern) || input == "") {
            Result.Success
        }else {
            Result.Error("Input contains invalid characters")
        }*/
        return Result.Success
    }

    fun checkStringWithoutSpace(input: String): Result {
        val pattern = Regex("[^\\s]+")
        return if (input.matches(pattern) || input == "") {
            Result.Success
        } else {
            Result.Error("Input contains invalid characters")
        }
    }

}
