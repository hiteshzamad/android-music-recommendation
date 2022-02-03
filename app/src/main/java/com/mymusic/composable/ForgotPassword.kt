package com.mymusic.composable

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mymusic.MAX_EMAIL_LENGTH
import com.mymusic.model.Task
import com.mymusic.viewmodel.ForgotPasswordVM

@Composable
fun ForgotPasswordComposable(
    viewModel: ForgotPasswordVM = viewModel(),
    onNavigateUpClicked: () -> Unit,
    onForgotSuccess: () -> Unit
) {
    var running = false
    val scrollState = rememberScrollState()
    var email by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    viewModel.forgot.observeAsState().value?.let { task ->
        when (task) {
            is Task.Init -> {}
            is Task.Running -> running = true
            is Task.Failed -> {
                running = false
                LaunchedEffect(task) {
                    task.message?.let { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            is Task.Success -> {
                running = false
                LaunchedEffect(task) {
                    Toast.makeText(context, "Email Sent", Toast.LENGTH_SHORT).show()
                    onForgotSuccess()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBarComposable(
                title = "Forgot Password",
                icon = Icons.Default.KeyboardArrowLeft,
                enabled = !running,
                onNavigateUpClicked = onNavigateUpClicked
            )
        },
        content = {
            Column {
                ProgressBarComposable(enabled = running)
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(scrollState)
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FormComposable(
                        enabled = !running,
                        email = email,
                        onEmailValueChange = {
                            if (it.length <= MAX_EMAIL_LENGTH) email = it
                        },
                        onForgotClicked = {
                            email = email.trim()
                            viewModel.forgotPassword(email)
                        },
                    )
                }
            }
        }
    )
}

@Composable
private fun FormComposable(
    enabled: Boolean,
    email: String,
    onEmailValueChange: (String) -> Unit,
    onForgotClicked: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    if (!enabled) {
        focusManager.clearFocus()
    }
    TextFieldComposable(
        value = email,
        enabled = enabled,
        leadingIcon = Icons.Default.AlternateEmail,
        onValueChange = onEmailValueChange,
        label = "Email",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        modifier = Modifier.fillMaxWidth()
    )
    SpacerComposable(60.dp)
    ButtonComposable(
        onClick = onForgotClicked,
        enabled = enabled,
        text = "Forgot Password"
    )
}
