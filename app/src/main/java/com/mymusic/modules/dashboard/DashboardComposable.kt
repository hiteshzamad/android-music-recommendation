package com.mymusic.modules.dashboard

import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.mymusic.modules.account.AccountComposable
import com.mymusic.modules.localmusic.LocalMusicComposable
import com.mymusic.modules.music.Music
import com.mymusic.modules.musichistory.MusicHistoryComposable
import com.mymusic.modules.musicplayer.MusicPlayerState
import com.mymusic.modules.recommendation.RecommendationComposable
import com.mymusic.modules.search.MusicSearch
import com.mymusic.modules.search.SearchComposable
import com.mymusic.navigation.*
import com.mymusic.util.DarkTopAppBarComposable
import com.mymusic.util.DialogButtonComposable
import com.mymusic.util.DialogTextComposable

@Composable
fun DashboardComposable(
    viewModel: DashboardViewModel = viewModel(),
    onLogOutClick: () -> Unit,
    onMusicExploreClicked: () -> Unit
) {
    var music: Music? = null
    var playerState = MusicPlayerState.PAUSE
    var historyList = listOf<Music>()
    var recommendationList = listOf<Music>()
    var searchList = listOf<MusicSearch>()
    var refreshRecommendation = false
    val (search, setSearch) = rememberSaveable { mutableStateOf("") }
    viewModel.refreshRecommendation.observeAsState().value?.let { refresh ->
        refreshRecommendation = refresh
    }
    viewModel.musicHistoryList.observeAsState().value?.let { list1 -> historyList = list1 }
    viewModel.currentMusic.observeAsState().value?.let { music1 -> music = music1 }
    viewModel.playerState.observeAsState().value?.let { playerState1 -> playerState = playerState1 }
    viewModel.recommendationList.observeAsState().value?.let { list1 -> recommendationList = list1 }
    viewModel.searchList.observeAsState().value?.data?.let { list1 -> searchList = list1 }
    val items = listOf(
        DashboardNavigation.Recommendation,
        DashboardNavigation.MusicSearch,
        DashboardNavigation.MusicHistory,
        DashboardNavigation.Account
    )
    val (dialogLogOutShow, setDialogLogOutShow) = remember { mutableStateOf(false) }
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        topBar = {
            navBackStackEntry?.destination?.route?.let {
                when (it) {
                    RECOMMENDATION -> DarkTopAppBarComposable(
                        title = "Recommendation"
                    )
                    MUSIC_HISTORY -> DarkTopAppBarComposable(
                        title = "History"
                    )
                    LOCAL_MUSIC -> DarkTopAppBarComposable(
                        title = "Local"
                    )
                    ACCOUNT -> DarkTopAppBarComposable(
                        title = "Account",
                        actionIcon = Icons.Default.Logout,
                        onActionClick = {
                            setDialogLogOutShow(true)
                        }
                    )
                    MUSIC_SEARCH -> TopAppBarSearch(
                        search = search,
                        setSearch = setSearch,
                        onSearch = { viewModel.search(search) }
                    )
                }
            }
        },
        bottomBar = {
            Column {
                music?.let { music1 ->
                    MusicPlayerTile(
                        music = music1,
                        musicPlayerState = playerState,
                        onPausePlayClicked = { play ->
                            if (play) {
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
                composable(RECOMMENDATION) {
                    RecommendationComposable(
                        recommendationList = recommendationList,
                        refresh = refreshRecommendation,
                        onRefresh = { viewModel.refreshRecommendation() },
                        onMusicClick = { music -> viewModel.start(music) }
                    )
                }
                composable(MUSIC_HISTORY) {
                    MusicHistoryComposable(historyList) { music: Music -> viewModel.start(music) }
                }
                composable(LOCAL_MUSIC) {
                    LocalMusicComposable(
                        onMusicClick = { music1: Music ->
                            viewModel.start(music = music1)
                        }
                    )
                }
                composable(ACCOUNT) {
                    AccountComposable()
                    DialogLogOutComposable(
                        show = dialogLogOutShow,
                        onConfirm = {
                            viewModel.logOut()
                            onLogOutClick()
                            setDialogLogOutShow(false)
                        },
                        onDismiss = {
                            setDialogLogOutShow(false)
                        }
                    )
                }
                composable(MUSIC_SEARCH) {
                    SearchComposable(
                        list = searchList,
                        onSelect = { musicSearch ->
                            viewModel.start(musicSearch)
                        }
                    )
                }
            }
        }
    )
}


@Composable
private fun MusicPlayerTile(
    music: Music,
    musicPlayerState: MusicPlayerState,
    onPausePlayClicked: (Boolean) -> Unit,
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
                LaunchedEffect(music.image) {
                    Glide.with(context)
                        .load(music.image)
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
            when (musicPlayerState) {
                MusicPlayerState.PAUSE, MusicPlayerState.PREPARED, MusicPlayerState.COMPLETED -> {
                    IconButton(onClick = { onPausePlayClicked(true) }) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
                MusicPlayerState.PLAY -> {
                    IconButton(onClick = { onPausePlayClicked(false) }) {
                        Icon(
                            imageVector = Icons.Default.Pause,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                }
                MusicPlayerState.LOADING -> {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun DialogLogOutComposable(
    show: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            text = {
                Column {
                    DialogTextComposable("Do You Want To Logout?")
                }
            },
            confirmButton = {
                DialogButtonComposable("Yes", onClick = onConfirm)
            },
            dismissButton = {
                DialogButtonComposable("No", onClick = onDismiss)
            }
        )
    }
}

@Composable
private fun TopAppBarSearch(
    search: String,
    setSearch: (String) -> Unit,
    onSearch: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    TopAppBar(
        title = {
            TextField(
                value = search,
                onValueChange = { value ->
                    if (value.length <= 32) {
                        setSearch(value)
                        if (search.isNotEmpty() && search.length >= 4 ) {
                            onSearch()
                        }
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.primarySurface,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (search.isNotEmpty()) {
                            onSearch()
                            focusManager.clearFocus()
                        }
                    }
                ),
                placeholder = { Text("Enter Song Name", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (search.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                setSearch("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            )

        }
    )
}