package com.mymusic.modules.account

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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
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
import com.mymusic.util.MAX_NAME_LENGTH
import com.mymusic.util.Task
import com.mymusic.util.*

@Composable
fun AddDetailComposable(
    viewModel: AddAccountDataViewModel = viewModel(),
    onAddDetailSuccess: () -> Unit
) {
    var running = false
    val scrollState = rememberScrollState()
    var name by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf("") }
    val genderOptions = listOf("Male", "Female")
    val (gender, onOptionSelected) = rememberSaveable { mutableStateOf(genderOptions[0]) }
    val context = LocalContext.current
    viewModel.addDetail.observeAsState().value?.let { task ->
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
                    Toast.makeText(context, "Details Added", Toast.LENGTH_SHORT).show()
                    onAddDetailSuccess()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBarComposable(
                title = "Add Details"
            )
        },
        content = {
            Column {
                ProgressBarComposable(enabled = running)
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(scrollState).padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FormComposable(
                        enabled = !running,
                        name = name,
                        dob = dob,
                        gender = gender,
                        radioOptions = genderOptions,
                        onNameValueChange = {
                            if (it.length <= MAX_NAME_LENGTH) name = it
                        },
                        onDobValueChange = {
                            dob = it
                        },
                        onOptionSelected = onOptionSelected,
                        onAddDetailClicked = {
                            name = name.trim()
                            viewModel.addDetail(name, gender, dob)
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
    name: String,
    dob: String,
    gender: String,
    radioOptions: List<String>,
    onNameValueChange: (String) -> Unit,
    onDobValueChange: (String) -> Unit,
    onOptionSelected: (String) -> Unit,
    onAddDetailClicked: () -> Unit,
) {
    var dialog by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    if (!enabled) {
        focusManager.clearFocus()
        dialog = false
    }
    DatePickerComposable(
        context = context,
        onDateSelected = onDobValueChange,
        enabled = dialog,
        onDismiss = {
            dialog = false
        },
    )
    TextFieldComposable(
        value = name,
        enabled = enabled,
        leadingIcon = Icons.Default.Person,
        onValueChange = onNameValueChange,
        label = "Name",
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        modifier = Modifier.fillMaxWidth()
    )
    SpacerComposable(20.dp)
    RadioGroupComposable(enabled, gender, onOptionSelected, radioOptions)
    SpacerComposable(20.dp)
    TextFieldButtonComposable(
        icon = Icons.Default.CalendarToday,
        title = "Date Of Birth",
        value = dob,
        onClick = {
            dialog = true
        }
    )
    SpacerComposable(40.dp)
    ButtonComposable(
        onClick = onAddDetailClicked,
        enabled = enabled,
        text = "Add Detail"
    )
}

