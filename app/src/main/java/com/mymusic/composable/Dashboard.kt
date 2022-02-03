package com.mymusic.composable

import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.Glide
import com.mymusic.R
import com.mymusic.model.Music
import com.mymusic.model.Task
import com.mymusic.navigation.*
import com.mymusic.viewmodel.DashboardVM

@Composable
fun DashboardComposable(
    viewModel: DashboardVM = viewModel(),
    onMusicExploreClicked: () -> Unit
) {
    var music: Music? = null
    var play = false
    var recommendationList = listOf<String>()
    var historyList = listOf<String>()
    val context = LocalContext.current
    viewModel.currentMusic.observeAsState().value?.let { music1 -> music = music1 }
    viewModel.musicRunning.observeAsState().value?.let { play1 -> play = play1 }
    viewModel.historyList.observeAsState().value?.data?.let { list1 -> historyList = list1 }
    viewModel.recommendationList.observeAsState().value?.let { task ->
        when (task) {
            is Task.Init, is Task.Running -> {}
            is Task.Failed -> {
                LaunchedEffect(task) {
                    task.message?.let { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            is Task.Success -> {
                task.data?.let {
                    recommendationList = it
                }
            }
        }
    }
    val items = listOf(
        DashboardNavigation.Home,
        DashboardNavigation.Device,
        DashboardNavigation.Account
    )
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        topBar = {
            navBackStackEntry?.destination?.route?.let {
                when (it) {
                    HOME -> DarkTopAppBarComposable(
                        title = "Home"
                    )
                    LOCAL_STORAGE -> DarkTopAppBarComposable(
                        title = "Local Storage"
                    )
                    ACCOUNT -> DarkTopAppBarComposable(
                        title = "Account"
                    )
                }
            }
        },
        floatingActionButton = {
            navBackStackEntry?.destination?.route?.let {
                if (it == HOME) {
                    FloatingActionButton(
                        onClick = {
                            viewModel.recommend()
                        },
                        content = {
                            Icon(Icons.Default.Memory, contentDescription = null)
                        }
                    )
                }
            }
        },
        bottomBar = {
            Column {
                music?.let { music1 ->
                    MusicPlayerTile(
                        music = music1,
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
                BottomNavigation {
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
        },
        content = {
            NavHost(
                navController = navHostController,
                startDestination = START_DESTINATION_DASHBOARD_SCREEN,
                modifier = Modifier.padding(it)
            ) {
                composable(HOME) {
                    HomeComposable(historyList, recommendationList)
                }
                composable(LOCAL_STORAGE) {
                    LocalStorageComposable(
                        onMusicClick = { music1 ->
                            viewModel.start(music = music1)
                        }
                    )
                }
                composable(ACCOUNT) {
                    AccountComposable()
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
    val context = LocalContext.current
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
            val imageView = remember { ImageView(context) }
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
