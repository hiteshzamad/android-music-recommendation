package com.mymusic

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mymusic.modules.account.AddDetailComposable
import com.mymusic.modules.account.ForgotPasswordComposable
import com.mymusic.modules.initialize.DeviceState
import com.mymusic.modules.account.SignUpComposable
import com.mymusic.modules.initialize.InitializeComposable
import com.mymusic.modules.account.LogInComposable
import com.mymusic.theme.AppTheme
import com.mymusic.modules.dashboard.DashboardComposable
import com.mymusic.modules.musicplayer.MusicPlayerComposable
import com.mymusic.modules.welcome.WelcomeComposable
import com.mymusic.navigation.*

@Composable
fun AppComposable() {
    val navHostController = rememberNavController()
    AppTheme {
        NavHost(
            navController = navHostController,
            startDestination = START_DESTINATION_APP
        ) {
            composable(INITIALIZE) {
                InitializeComposable(
                    onInitialize = { state ->
                        when (state) {
                            DeviceState.NOT_LOGGED -> navHostController.navigate(WELCOME) {
                                popUpTo(INITIALIZE) { inclusive = true }
                            }
                            DeviceState.NO_DETAIL -> navHostController.navigate(ADD_DETAIL) {
                                popUpTo(INITIALIZE) { inclusive = true }
                            }
                            DeviceState.LOGGED -> navHostController.navigate(DASHBOARD) {
                                popUpTo(INITIALIZE) { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable(WELCOME) {
                WelcomeComposable(
                    onLoginClicked = {
                        navHostController.navigate(LOG_IN)
                    },
                    onSignUpClicked = {
                        navHostController.navigate(SIGN_UP)
                    }
                )
            }
            composable(LOG_IN) {
                LogInComposable(
                    onNavigateUpClicked = {
                        navHostController.navigateUp()
                    },
                    onLoginSuccess = {
                        navHostController.navigate(INITIALIZE) {
                            popUpTo(WELCOME) { inclusive = true }
                        }
                    },
                    onForgotClicked = {
                        navHostController.navigate(FORGOT)
                    }
                )
            }
            composable(SIGN_UP) {
                SignUpComposable(
                    onNavigateUpClicked = {
                        navHostController.navigateUp()
                    },
                    onSignUpSuccess = {
                        navHostController.navigate(INITIALIZE) {
                            popUpTo(WELCOME) { inclusive = true }
                        }
                    }
                )
            }
            composable(ADD_DETAIL) {
                AddDetailComposable(
                    onAddDetailSuccess = {
                        navHostController.navigate(DASHBOARD) {
                            popUpTo(ADD_DETAIL) { inclusive = true }
                        }
                    }
                )
            }
            composable(FORGOT) {
                ForgotPasswordComposable(
                    onNavigateUpClicked = {
                        navHostController.navigateUp()
                    },
                    onForgotSuccess = {
                        navHostController.navigateUp()
                    }
                )
            }
            composable(DASHBOARD) {
                DashboardComposable(
                    onMusicExploreClicked = {
                        navHostController.navigate(PLAYER)
                    }
                )
            }
            composable(PLAYER) {
                MusicPlayerComposable(
                    onNavigationUpClicked = {
                        navHostController.navigateUp()
                    }
                )
            }
        }
    }
}
