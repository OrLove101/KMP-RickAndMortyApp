package com.orlove.mortyapp.util

fun formatDate(dateString: String): String {
    return try {
        dateString.substringBefore('T')
    } catch (e: Exception) {
        dateString
    }
}