package com.mymusic.login

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mymusic.MAX_EMAIL_LENGTH
import com.mymusic.MAX_PASSWORD_LENGTH
import com.mymusic.model.Task
import com.mymusic.viewmodel.LogInVM

@Composable
fun LogInComposable(
    viewModel: LogInVM = viewModel(),
    onNavigateUpClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
    onForgotClicked: () -> Unit
) {
    var running = false
    val scrollState = rememberScrollState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    viewModel.logIn.observeAsState().value?.let { task ->
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
                    Toast.makeText(context, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBarComposable(
                title = "Log In",
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
                        password = password,
                        onEmailValueChange = {
                            if (it.length <= MAX_EMAIL_LENGTH) email = it
                        },
                        onPasswordValueChange = {
                            if (it.length <= MAX_PASSWORD_LENGTH) password = it
                            password = password.trim()
                        },
                        onLoginClicked = {
                            email = email.trim()
                            password = password.trim()
                            viewModel.logIn(email, password)
                        },
                        onForgotClicked = onForgotClicked
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
    password: String,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onForgotClicked: () -> Unit,
    onLoginClicked: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    if (!enabled) {
        focusManager.clearFocus()
    }
    val passwordFocusRequester = remember { FocusRequester() }
    TextFieldComposable(
        value = email,
        enabled = enabled,
        leadingIcon = Icons.Default.AlternateEmail,
        onValueChange = onEmailValueChange,
        label = "Email",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
        modifier = Modifier.fillMaxWidth()
    )
    SpacerComposable(20.dp)
    TextFieldPasswordComposable(
        value = password,
        enabled = enabled,
        onValueChange = onPasswordValueChange,
        label = "Password",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        modifier = Modifier.focusRequester(focusRequester = passwordFocusRequester).fillMaxWidth()
    )
    SpacerComposable(5.dp)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        TextButtonComposable(
            onClick = onForgotClicked,
            text = "Forgot Password",
            enabled = enabled
        )
    }
    SpacerComposable(60.dp)
    ButtonComposable(
        onClick = onLoginClicked,
        enabled = enabled,
        text = "Log In"
    )
}
