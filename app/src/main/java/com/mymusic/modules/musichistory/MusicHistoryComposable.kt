package com.mymusic.modules.musichistory

import android.widget.ImageView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mymusic.R
import com.mymusic.modules.music.Music
import com.mymusic.util.SpacerComposable

@Composable
fun MusicHistoryComposable(
    historyList: List<Music>,
    onMusicClick: (Music) -> Unit
) {
    View(historyList, onMusicClick)
}

@Composable
private fun View(musicList: List<Music>, onMusicClick: (Music) -> Unit) {
    if (musicList.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.FormatListBulleted,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )
            SpacerComposable(i = 5.dp)
            Text(text = "No History", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(musicList) { item ->
                MusicItem(
                    name = item.name,
                    artist = item.artist,
                    imageUri = item.image,
                    onClick = {
                        onMusicClick(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun MusicItem(name: String, artist: String, imageUri: String, onClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        val imageView = remember {
            ImageView(context)
        }
        Column(
            modifier = Modifier
                .size(60.dp)
                .padding(2.dp)
        ) {
            AndroidView(
                factory = { imageView },
                update = {}
            )
        }
        LaunchedEffect(imageUri) {
            Glide.with(context)
                .load(imageUri)
                .error(R.drawable.app_icon)
                .override(200, 200)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView)
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .height(60.dp)
        ) {
            Text(
                text = name,
                fontSize = 16.sp,
                maxLines = 1,
                fontFamily = FontFamily(Font(R.font.josefinsans_regular))
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = artist,
                fontSize = 14.sp,
                maxLines = 1,
                color = Color.DarkGray,
                fontFamily = FontFamily(Font(R.font.josefinsans_regular))
            )
        }
    }
}
