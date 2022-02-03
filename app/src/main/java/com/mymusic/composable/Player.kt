package com.mymusic.composable

import android.content.Context
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
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
import com.mymusic.R
import com.mymusic.model.Music
import com.mymusic.viewmodel.PlayerVM

@Composable
fun MusicPlayerComposable(
    viewModel: PlayerVM = viewModel(),
    onNavigationUpClicked: () -> Unit
) {
    var music: Music? = null
    var cursorPosition = 0F
    var play = false
    var loop = false
    var shuffle = false
    viewModel.currentMusic.observeAsState().value?.let { music1 ->
        music = music1
    }
    viewModel.cursorPosition.observeAsState().value?.let { float ->
        cursorPosition = float
    }
    viewModel.play.observeAsState().value?.let { boolean ->
        play = boolean
    }
    viewModel.loop.observeAsState().value?.let { boolean ->
        loop = boolean
    }
    viewModel.shuffle.observeAsState().value?.let { boolean ->
        shuffle = boolean
    }
    music?.let { music1 ->
        Screen(
            imageUri = music1.imageUri,
            name = music1.name,
            artist = music1.artist,
            cursorPosition = cursorPosition,
            duration = music1.duration,
            play = play,
            shuffle = shuffle,
            loop = loop,
            onMusicPositionChange = { position ->
                viewModel.cursorPosition(position)
            },
            onNavigationUpClicked = onNavigationUpClicked,
            onPlayPauseClicked = {
                if (!play) {
                    viewModel.play()
                } else {
                    viewModel.pause()
                }
            },
            onLoopClicked = {
                if (!loop) {
                    viewModel.loop()
                } else {
                    viewModel.unLoop()
                }
            },
            onShuffleClicked = {
                if (shuffle) {
                    viewModel.unShuffle()
                } else {
                    viewModel.shuffle()
                }
            },
            onPreviousClicked = {
                viewModel.previousMusic()
            },
            onNextClicked = {
                viewModel.nextMusic()
            },
            onPlayListClicked = {

            }
        )
    }
}

@Composable
private fun Screen(
    imageUri: String,
    name: String,
    artist: String,
    cursorPosition: Float,
    duration: Float,
    play: Boolean,
    loop: Boolean,
    shuffle: Boolean,
    onMusicPositionChange: (Float) -> Unit,
    onNavigationUpClicked: () -> Unit,
    onPlayPauseClicked: () -> Unit,
    onLoopClicked: () -> Unit,
    onShuffleClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onPlayListClicked: () -> Unit

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionBar(onNavigationUpClicked)
        MusicImage(imageUri = imageUri)
        MusicDetail(name = name, artist = artist)
        MusicController(
            cursorPosition = cursorPosition,
            duration = duration,
            play = play,
            loop = loop,
            shuffle = shuffle,
            onMusicPositionChange = onMusicPositionChange,
            onPlayPauseClicked = onPlayPauseClicked,
            onLoopClicked = onLoopClicked,
            onShuffleClicked = onShuffleClicked,
            onNextClicked = onNextClicked,
            onPreviousClicked = onPreviousClicked,
            onPlayListClicked = onPlayListClicked
        )
    }
}


@Composable
private fun ActionBar(onNavigationUpClicked: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onNavigationUpClicked) {
                Icon(
                    imageVector = Icons.Default.NavigateBefore,
                    contentDescription = null
                )
            }
        },
        elevation = 0.dp
    )
}

@Composable
private fun MusicImage(imageUri: String) {
    val context = LocalContext.current
    val imageView = rememberImageView(context = context)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.0f)
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp, top = 0.dp)
    ) {
        AndroidView(factory = { imageView }, update = {})
        Glide.with(context).load(imageUri)
            .error(AppCompatResources.getDrawable(context, R.drawable.app_icon))
            .fitCenter()
            .into(imageView)
    }
}

@Composable
private fun rememberImageView(context: Context) = remember {
    ImageView(context)
}

@Composable
private fun MusicDetail(name: String, artist: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = name,
            fontSize = 20.sp,
            maxLines = 1,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.josefinsans_regular))
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            text = artist,
            fontSize = 16.sp,
            maxLines = 1,
            color = Color.LightGray,
            fontFamily = FontFamily(Font(R.font.josefinsans_regular))
        )
    }
}

@Composable
private fun MusicController(
    cursorPosition: Float,
    duration: Float,
    play: Boolean,
    loop: Boolean,
    shuffle: Boolean,
    onMusicPositionChange: (Float) -> Unit,
    onPlayPauseClicked: () -> Unit,
    onLoopClicked: () -> Unit,
    onShuffleClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onPlayListClicked: () -> Unit

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var input by remember { mutableStateOf(false) }
        var sliderValue by remember { mutableStateOf(0F) }
        if (!input) {
            sliderValue = cursorPosition
        }
        Slider(
            value = sliderValue,
            onValueChange = { float ->
                input = true
                sliderValue = float
                onMusicPositionChange(sliderValue)
            },
            onValueChangeFinished = {
                input = false
            },
            valueRange = 0f..duration,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = String.format(
                    "%d:%02d",
                    cursorPosition.toInt() / 60,
                    cursorPosition.toInt() % 60
                ),
                color = Color.White,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.weight(1.0f))
            Text(
                text = String.format("%d:%02d", duration.toInt() / 60, duration.toInt() % 60),
                color = Color.White,
                fontSize = 12.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onShuffleClicked) {
                Icon(
                    imageVector = if (shuffle) {
                        Icons.Default.ShuffleOn
                    } else {
                        Icons.Default.Shuffle
                    },
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(0.2f))
            IconButton(onClick = onPreviousClicked) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(0.1f))
            IconButton(onClick = onPlayPauseClicked) {
                Icon(
                    imageVector = if (play) {
                        Icons.Default.PauseCircleOutline
                    } else {
                        Icons.Default.PlayCircleOutline
                    },
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(0.1f))
            IconButton(onClick = onNextClicked) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(0.2f))
            IconButton(onClick = onLoopClicked) {
                Icon(
                    imageVector = if (loop) {
                        Icons.Default.RepeatOn
                    } else {
                        Icons.Default.Repeat
                    },
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onPlayListClicked) {
                Icon(
                    imageVector = Icons.Default.PlaylistPlay,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}
