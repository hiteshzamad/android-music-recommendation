package com.mymusic.modules.dashboard

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.ui.graphics.vector.ImageVector
import com.mymusic.R
import com.mymusic.navigation.ACCOUNT
import com.mymusic.navigation.HISTORY
import com.mymusic.navigation.HOME
import com.mymusic.navigation.LOCAL

sealed class DashboardNavigation(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object Home : DashboardNavigation(HOME, R.string.home, Icons.Default.Home)
    object History : DashboardNavigation(HISTORY, R.string.history, Icons.Outlined.History)
    object Local : DashboardNavigation(LOCAL, R.string.local, Icons.Outlined.Storage)
    object Account : DashboardNavigation(ACCOUNT, R.string.account, Icons.Default.AccountCircle)
}