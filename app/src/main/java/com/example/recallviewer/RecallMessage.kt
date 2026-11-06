package com.example.recallviewer

data class RecallMessage(
    val id: Long = 0,
    val sender: String,
    val content: String,
    val packageName: String,
    val timestamp: Long,
    val isRecalled: Boolean = false
)
