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
                            DeviceState.LOGGED -> navHostController.navigate(DASHBOARD) {
                                popUpTo(INITIALIZE) { inclusive = true }
                            }
                        }
                    }
                )
            }
            composable(NOT_LOGGED) {

            }
            composable(LOG_IN) {

            }
            composable(REGISTER) {

            }
            composable(DASHBOARD) {

            }
        }
    }
}
