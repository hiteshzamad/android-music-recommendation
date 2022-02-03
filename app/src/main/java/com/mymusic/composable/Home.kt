package com.mymusic.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeComposable(
    historyList: List<String>,
    recommendationList: List<String>
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(0.5f).padding(10.dp)
        ) {
            View("History", historyList)
        }
        Column(
            modifier = Modifier.fillMaxWidth().weight(0.5f).padding(10.dp)
        ) {
            View("Recommendation", recommendationList)
        }
    }
}

@Composable
private fun View(title: String, list: List<String>) {
    Card {
        Column {
            Text(
                title,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.primary)
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(list) { item ->
                    Column {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = item,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(
                            modifier = Modifier.fillMaxWidth().height(1.dp)
                                .background(Color.LightGray)
                        )
                    }
                }
            }
        }
    }
}

