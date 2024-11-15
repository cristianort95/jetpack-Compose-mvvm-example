package com.example.myapplication.view.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TextMedium (text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray
    )
}