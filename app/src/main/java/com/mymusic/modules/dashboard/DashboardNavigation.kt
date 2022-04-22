package com.mymusic.modules.dashboard

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.ui.graphics.vector.ImageVector
import com.mymusic.R
import com.mymusic.navigation.*

sealed class DashboardNavigation(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object Recommendation : DashboardNavigation(RECOMMENDATION, R.string.recommend, Icons.Default.Home)
    object MusicHistory : DashboardNavigation(MUSIC_HISTORY, R.string.history, Icons.Outlined.History)
    object MusicSearch : DashboardNavigation(MUSIC_SEARCH, R.string.search, Icons.Outlined.Search)
    object LocalMusic : DashboardNavigation(LOCAL_MUSIC, R.string.device, Icons.Outlined.Storage)
    object Account : DashboardNavigation(ACCOUNT, R.string.account, Icons.Default.AccountCircle)
}