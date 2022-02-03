package com.mymusic.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NotLoggedComposable(
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
        modifier = Modifier.fillMaxSize().padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Welcome",
            fontSize = 26.sp,
            modifier = Modifier.height(30.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(100.dp))
        ButtonComposable(onClick = onLoginClicked, "Log In")
        Spacer(modifier = Modifier.height(20.dp))
        ButtonComposable(onClick = onRegisterClicked, "Sign Up")
        Spacer(modifier = Modifier.height(50.dp))
    }
}