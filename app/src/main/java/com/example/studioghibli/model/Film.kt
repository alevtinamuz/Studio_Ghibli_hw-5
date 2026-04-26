package com.example.studioghibli.model

import java.util.Locale

data class Film(
    val id: String,
    val title: String,
    val originalTitle: String,
    val description: String,
    val director: String,
    val producer: String,
    val releaseDate: String,
    val runningTime: Int,
    val rtScore: String,
    val isFavourite: Boolean = false,
) {
    val formattedTime: String
        get() {
            val hours = runningTime / 60
            val minutes = runningTime % 60
            return if (hours > 0) {
                String.format(Locale.US, "%d ч %02d мин", hours, minutes)
            } else {
                String.format(Locale.US, "%d мин", minutes)
            }
        }
}