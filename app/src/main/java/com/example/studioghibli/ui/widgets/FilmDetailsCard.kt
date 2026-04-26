package com.example.studioghibli.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.studioghibli.model.Film

@Composable
fun FilmDetailsCard(
    film: Film
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = film.title, fontWeight = FontWeight.Bold)
        Text(
            text = "Оригинальное название: ${film.originalTitle}",
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Режиссёр: ${film.director}")
        Text(text = "Продюсер: ${film.producer}")
        Text(text = "Дата выхода: ${film.releaseDate}")
        Text(text = "Длительность: ${film.formattedTime}")
        Text(text = "Рейтинг: ${film.rtScore}")
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Описание: ${film.description}")
    }
}