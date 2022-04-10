package com.mymusic.modules.musichistory

import android.widget.ImageView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mymusic.R
import com.mymusic.model.Music

@Composable
fun MusicHistoryComposable(
    viewModel : MusicHistoryViewModel = viewModel(),
    onMusicClick: (Music) -> Unit
) {
    var historyList = listOf<Music>()
    viewModel.historyList.observeAsState().value?.data?.let { list1 -> historyList = list1 }
    View(historyList, onMusicClick)
}

@Composable
private fun View(musicList: List<Music>, onMusicClick: (Music) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(musicList) { item ->
            MusicItem(
                name = item.name,
                artist = item.artist,
                imageUri = item.imageUri,
                onClick = {
                    onMusicClick(item)
                }
            )
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
