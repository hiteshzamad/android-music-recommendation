package com.mymusic.composable

import android.content.Context
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.Glide
import com.mymusic.R
import com.mymusic.model.Music
import com.mymusic.navigation.*
import com.mymusic.viewmodel.DashboardViewModel

@Composable
fun DashboardComposable(
    viewModel: DashboardViewModel = viewModel(),
    onMusicExploreClicked: () -> Unit
) {
    var music: Music? = null
    var play = false
    viewModel.currentMusic.observeAsState().value?.let { music1 -> music = music1 }
    viewModel.play.observeAsState().value?.let { play1 -> play = play1 }
    Screen(
        onMusicClicked = { music1 ->
            viewModel.start(music = music1)
        },
        currentMusic = music,
        play = play,
        onPausePlayClicked = {
            if (!play) {
                viewModel.play()
            } else {
                viewModel.pause()
            }
        },
        onMusicExploreClicked = onMusicExploreClicked
    )
}

@Composable
private fun Screen(
    onMusicClicked: (Music) -> Unit,
    currentMusic: Music?,
    play: Boolean,
    onPausePlayClicked: () -> Unit,
    onMusicExploreClicked: () -> Unit
) {
    val navHostController = rememberNavController()
    Scaffold(
        bottomBar = {
            Column {
                currentMusic?.let { currentMusic1 ->
                    MusicPlayerTile(
                        music = currentMusic1,
                        play = play,
                        onPausePlayClicked = onPausePlayClicked,
                        onMusicExploreClicked = onMusicExploreClicked
                    )
                }
                BottomNavigation(navHostController = navHostController)
            }
        },
        content = {
            NavHost(
                navController = navHostController,
                startDestination = START_DESTINATION_DASHBOARD_SCREEN,
                modifier = Modifier.padding(it)
            ) {
                composable(HOME) {

                }
                composable(STORAGE) {
                    StorageComposable(
                        onMusicClick = onMusicClicked
                    )
                }
                composable(ACCOUNT) {

                }
            }
        }
    )
}

@Composable
private fun MusicPlayerTile(
    music: Music,
    play: Boolean,
    onPausePlayClicked: () -> Unit,
    onMusicExploreClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(all = 4.dp)
            .fillMaxWidth()
            .height(55.dp)
            .clickable { onMusicExploreClicked() }
    ) {
        Row(
            modifier = Modifier.background(color = MaterialTheme.colors.primary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageView = rememberImageView(context = LocalContext.current)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1.0f)
                    .padding(8.dp)
            ) {
                AndroidView(
                    factory = { imageView },
                    update = {}
                )
                val context = LocalContext.current
                LaunchedEffect(music.imageUri) {
                    Glide.with(context)
                        .load(music.imageUri)
                        .error(R.drawable.app_icon)
                        .override(100, 100)
                        .fitCenter()
                        .into(imageView)
                }
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1.0f)
            ) {
                Text(
                    text = music.name,
                    fontSize = 14.sp,
                    maxLines = 1,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.josefinsans_regular))
                )
                Text(
                    text = music.artist,
                    fontSize = 12.sp,
                    maxLines = 1,
                    color = Color.LightGray,
                    fontFamily = FontFamily(Font(R.font.josefinsans_regular))
                )
            }
            IconButton(
                onClick = onPausePlayClicked
            ) {
                Icon(
                    imageVector = if (play) {
                        Icons.Default.Pause
                    } else {
                        Icons.Default.PlayArrow
                    },
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun rememberImageView(context: Context) = remember {
    ImageView(context)
}

@Composable
private fun BottomNavigation(navHostController: NavHostController) {
    val items = listOf(
        DashboardNavigation.Home,
        DashboardNavigation.Device,
        DashboardNavigation.Account
    )
    BottomNavigation {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navHostController.navigate(screen.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
