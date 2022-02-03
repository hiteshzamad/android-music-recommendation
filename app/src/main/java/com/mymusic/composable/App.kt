package com.mymusic.composable

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mymusic.model.DeviceState
import com.mymusic.navigation.*
import com.mymusic.theme.AppTheme

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
                            DeviceState.NOT_LOGGED -> navHostController.navigate(NOT_LOGGED) {
                                popUpTo(INITIALIZE) { inclusive = true }
                            }
                            DeviceState.DETAIL -> navHostController.navigate(ADD_DETAIL) {
                                popUpTo(INITIALIZE) { inclusive = true }
                            }
                            DeviceState.LOGGED -> navHostController.navigate(DASHBOARD) {
                                popUpTo(INITIALIZE) { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable(NOT_LOGGED) {
                NotLoggedComposable(
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
                            popUpTo(NOT_LOGGED) { inclusive = true }
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
                            popUpTo(NOT_LOGGED) { inclusive = true }
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
