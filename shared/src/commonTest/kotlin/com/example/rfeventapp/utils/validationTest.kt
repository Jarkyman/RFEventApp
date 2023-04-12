package com.example.rfeventapp.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class CommonValidationTest {
    @Test
    fun `test checkString with valid input _ Success`() {
        val input = "HelloWorld123"
        val result = Validation().checkString(input)
        assertEquals(Result.Success, result)
    }

    @Test
    fun `test checkString with valid input with space _ Success`() {
        val input = "Hello World 123"
        val result = Validation().checkString(input)
        assertEquals(Result.Success, result)
    }

    @Test
    fun `test checkString with invalid input _ Error`() {
        val input = "Hello@World"
        val result = Validation().checkString(input)
        assertEquals(Result.Error("Input contains invalid characters").toString(), result.toString())
    }

    @Test
    fun `test checkString with more invalid input _ Error`() {
        val input = "&/3#d"
        val result = Validation().checkString(input)
        assertEquals(Result.Error("Input contains invalid characters").toString(), result.toString())
    }

    @Test
    fun `test checkString with danish characters _ Success`() {
        val input = "Åen med ø og æ"
        val result = Validation().checkString(input)
        assertEquals(Result.Success, result)
    }

    @Test
    fun `test checkString with empty String _ Success`() {
        val input = ""
        val result = Validation().checkString(input)
        assertEquals(Result.Success, result)
    }

    @Test
    fun `test checkStringWithTap with empty String _ Success`() {
        val input = ""
        val result = Validation().checkStringWithTap(input)
        assertEquals(Result.Success, result)
    }

    @Test
    fun `test checkStringWithTap with tap _ Success`() {
        val input = "hej\t Test"
        val result = Validation().checkStringWithTap(input)
        assertEquals(Result.Success, result)
    }

    @Test
    fun `test checkStringWithTap with new line _ Success`() {
        val input = "hej\n Test"
        val result = Validation().checkStringWithTap(input)
        assertEquals(Result.Success, result)
    }

    @Test
    fun `test checkStringWithTap with Emoji _ Success`() {
        val input = "Emoji 😃"
        val result = Validation().checkStringWithTap(input)
        assertEquals(Result.Success, result)
    }

    @Test
    fun `test checkStringWithoutSpace with input _ Success`() {
        val input = "Username"
        val result = Validation().checkStringWithoutSpace(input)
        assertEquals(Result.Success, result)
    }

    @Test
    fun `test checkStringWithoutSpace with character _ Success`() {
        val input = "User@mail.com"
        val result = Validation().checkStringWithoutSpace(input)
        assertEquals(Result.Success, result)
    }

    @Test
    fun `test checkStringWithoutSpace with newline _ Error`() {
        val input = "Kodeord\n"
        val result = Validation().checkStringWithoutSpace(input)
        assertEquals(Result.Error("Input contains invalid characters").toString(), result.toString())
    }

    @Test
    fun `test checkStringWithoutSpace with only newline _ Error`() {
        val input = "\n"
        val result = Validation().checkStringWithoutSpace(input)
        assertEquals(Result.Error("Input contains invalid characters").toString(), result.toString())
    }

    @Test
    fun `test checkStringWithoutSpace with only space _ Error`() {
        val input = " "
        val result = Validation().checkStringWithoutSpace(input)
        assertEquals(Result.Error("Input contains invalid characters").toString(), result.toString())
    }

    @Test
    fun `test checkStringWithoutSpace with empty _ Success`() {
        val input = ""
        val result = Validation().checkStringWithoutSpace(input)
        assertEquals(Result.Success, result)
    }
}