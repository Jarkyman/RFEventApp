package com.example.rfeventapp.service

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}