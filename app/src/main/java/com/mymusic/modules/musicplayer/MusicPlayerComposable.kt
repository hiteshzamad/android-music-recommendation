package com.mymusic.modules.musicplayer

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
import com.mymusic.modules.download.DownloadState
import com.mymusic.modules.music.Music

@Composable
fun MusicPlayerComposable(
    viewModel: MusicPlayerViewModel = viewModel(),
    onNavigationUpClicked: () -> Unit
) {
    var music: Music? = null
    var cursor = 0F
    var duration = 0F
    var downloadState = DownloadState.NOT_DOWNLOADED
    var playerState = MusicPlayerState.PAUSE
    var loop = false
    var shuffle = false
    viewModel.downloadState.observeAsState().value?.let { downloadState1: DownloadState ->
        downloadState = downloadState1
    }
    viewModel.music.observeAsState().value?.let { music1: Music ->
        music = music1
    }
    viewModel.cursor.observeAsState().value?.let { float ->
        cursor = float
    }
    viewModel.duration.observeAsState().value?.let { d ->
        duration = d
    }
    viewModel.playerState.observeAsState().value?.let { playerState1 ->
        playerState = playerState1
    }
    viewModel.loop.observeAsState().value?.let { boolean ->
        loop = boolean
    }
    viewModel.shuffle.observeAsState().value?.let { boolean ->
        shuffle = boolean
    }
    music?.let { music1 ->
        Screen(
            music = music1,
            downloadState = downloadState,
            cursor = cursor,
            duration = duration,
            playerState = playerState,
            shuffle = shuffle,
            loop = loop,
            onMusicPositionChange = { position ->
                viewModel.cursor(position)
            },
            onNavigationUpClicked = onNavigationUpClicked,
            onPlayPauseClicked = { play ->
                if (play) {
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

            },
            onDownloadClick = {
                viewModel.downloadMusic(music1.path, "${music1.name} ${music1.artist}")
            }
        )
    }
}

@Composable
private fun Screen(
    music: Music,
    downloadState: DownloadState,
    cursor: Float,
    duration: Float,
    playerState: MusicPlayerState,
    loop: Boolean,
    shuffle: Boolean,
    onMusicPositionChange: (Float) -> Unit,
    onNavigationUpClicked: () -> Unit,
    onPlayPauseClicked: (Boolean) -> Unit,
    onLoopClicked: () -> Unit,
    onShuffleClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onPlayListClicked: () -> Unit,
    onDownloadClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionBar(onNavigationUpClicked)
        MusicImage(image = music.image)
        MusicDetail(name = music.name, artist = music.artist)
        MusicController(
            cursorPosition = cursor,
            duration = duration,
            playerState = playerState,
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
private fun ActionBar(
    onNavigationUpClicked: () -> Unit
) {
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
private fun MusicImage(image: String) {
    val context = LocalContext.current
    val imageView = rememberImageView(context = context)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.0f)
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp, top = 0.dp)
    ) {
        AndroidView(factory = { imageView }, update = {})
        Glide.with(context).load(image)
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
            fontSize = 15.sp,
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
    playerState: MusicPlayerState,
    loop: Boolean,
    shuffle: Boolean,
    onMusicPositionChange: (Float) -> Unit,
    onPlayPauseClicked: (Boolean) -> Unit,
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
            when (playerState) {
                MusicPlayerState.PAUSE, MusicPlayerState.PREPARED, MusicPlayerState.COMPLETED -> {
                    IconButton(onClick = { onPlayPauseClicked(true) }) {
                        Icon(
                            imageVector = Icons.Default.PlayCircleOutline,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
                MusicPlayerState.PLAY -> {
                    IconButton(onClick = { onPlayPauseClicked(false) }) {
                        Icon(
                            imageVector = Icons.Default.PauseCircleOutline,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
                MusicPlayerState.LOADING -> {
                    Box(
                        modifier = Modifier.size(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = Color.White
                        )
                    }
                }
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
    }
}
