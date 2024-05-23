package com.example.eventos

import java.text.SimpleDateFormat
import java.util.Locale

fun String.getDateTime(): String {
    return try {
        SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()).format(this.toLong()
        )
    } catch (e: Exception) {
        e.toString()
    }
}