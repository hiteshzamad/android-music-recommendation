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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mymusic.MAX_EMAIL_LENGTH
import com.mymusic.MAX_PASSWORD_LENGTH
import com.mymusic.model.Task
import com.mymusic.viewmodel.SignUpVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpComposable(
    viewModel: SignUpVM = viewModel(),
    onNavigateUpClicked: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    var running = false
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    viewModel.signUp.observeAsState().value?.let { task ->
        when (task) {
            is Task.Init -> {}
            is Task.Running -> running = true
            is Task.Failed -> {
                LaunchedEffect(task) {
                    running = false
                    task.message?.let { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            is Task.Success -> {
                LaunchedEffect(task) {
                    running = false
                    Toast.makeText(context, "Sign Up Successfully", Toast.LENGTH_SHORT).show()
                    onSignUpSuccess()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBarComposable(
                title = "Sign Up",
                icon = Icons.Default.KeyboardArrowLeft,
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
                        confirmPassword = confirmPassword,
                        onEmailValueChange = {
                            if (it.length <= MAX_EMAIL_LENGTH) email = it
                        },
                        onPasswordValueChange = {
                            if (it.length <= MAX_PASSWORD_LENGTH) password = it
                            password = password.trim()
                        },
                        onConfirmPasswordValueChange = {
                            if (it.length <= MAX_PASSWORD_LENGTH) confirmPassword = it
                            confirmPassword = confirmPassword.trim()
                        },
                        onFocusItemChanged = { value ->
                            coroutineScope.launch {
                                delay(600)
                                scrollState.animateScrollTo(value)
                            }
                        },
                        onSubmitClicked = {
                            email = email.trim()
                            password = password.trim()
                            confirmPassword = confirmPassword.trim()
                            viewModel.signUp(email, password, confirmPassword)
                        }
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
    confirmPassword: String,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onConfirmPasswordValueChange: (String) -> Unit,
    onFocusItemChanged: (Int) -> Unit,
    onSubmitClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    if (!enabled) {
        focusManager.clearFocus()
    }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }
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
        modifier = Modifier.onFocusChanged { focusState ->
            if (focusState.isFocused || focusState.isCaptured) {
                onFocusItemChanged(0)
            }
        }.fillMaxWidth()
    )
    SpacerComposable(20.dp)
    TextFieldPasswordComposable(
        value = password,
        enabled = enabled,
        onValueChange = onPasswordValueChange,
        label = "Password",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { confirmPasswordFocusRequester.requestFocus() }),
        modifier = Modifier.onFocusChanged { focusState ->
            if (focusState.isFocused || focusState.isCaptured) {
                onFocusItemChanged(0)
            }
        }.focusRequester(focusRequester = passwordFocusRequester).fillMaxWidth()
    )
    SpacerComposable(20.dp)
    TextFieldPasswordComposable(
        value = confirmPassword,
        enabled = enabled,
        onValueChange = onConfirmPasswordValueChange,
        label = "Confirm Password",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        modifier = Modifier.onFocusChanged { focusState ->
            if (focusState.isFocused || focusState.isCaptured) {
                onFocusItemChanged(100)
            }
        }.focusRequester(focusRequester = confirmPasswordFocusRequester).fillMaxWidth()
    )
    SpacerComposable(40.dp)
    ButtonComposable(
        onClick = onSubmitClicked,
        enabled = enabled,
        text = "Sign Up"
    )
}

