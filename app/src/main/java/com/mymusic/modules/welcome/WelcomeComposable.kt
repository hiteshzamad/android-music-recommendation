package com.mymusic.modules.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mymusic.util.ButtonComposable

@Composable
fun WelcomeComposable(
    onLoginClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {
    View(onLoginClicked, onSignUpClicked)
}

@Composable
private fun View(
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "Welcome\nTo\n${stringResource(id = com.mymusic.R.string.app_name)}",
            fontSize = 40.sp,
            modifier = Modifier.padding(horizontal = 40.dp, vertical = 80.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp),
        ) {
            ButtonComposable(onClick = onLoginClicked, "Log In")
            Spacer(modifier = Modifier.height(40.dp))
            ButtonComposable(onClick = onRegisterClicked, "Sign Up")
        }
    }
}