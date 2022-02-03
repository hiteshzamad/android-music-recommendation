package com.mymusic.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.ui.graphics.vector.ImageVector
import com.mymusic.R

sealed class DashboardNavigation(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object Home : DashboardNavigation(HOME, R.string.home, Icons.Default.Home)
    object Device : DashboardNavigation(LOCAL_STORAGE, R.string.local_storage, Icons.Outlined.Storage)
    object Account : DashboardNavigation(ACCOUNT, R.string.account, Icons.Default.AccountCircle)
}