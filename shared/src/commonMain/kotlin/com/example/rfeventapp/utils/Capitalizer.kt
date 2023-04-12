package com.example.rfeventapp.utils

class Capitalizer {
    fun capitalizeFirstLetter(str: String): String {
        if (str.isEmpty()) {
            return str
        }
        val firstChar = str[0].uppercaseChar()
        val restStr = str.substring(1).lowercase()
        return "$firstChar$restStr"
    }

    fun capitalizeWords(input: String): String {
        val words = input.split(" ")
        val capitalizedWords = words.map { it.capitalize() }
        return capitalizedWords.joinToString(" ")
    }

    fun capitalizeSentences(input: String): String {
        val sentences = input.split(". ", ".", "\n", "!", "?")
        val capitalizedSentences = sentences.map { sentence ->
            sentence.trim().lowercase().replaceFirstChar { it.uppercase() }
        }
        return capitalizedSentences.joinToString(". ") { "$it." }
    }

}