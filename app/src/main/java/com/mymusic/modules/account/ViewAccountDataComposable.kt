package com.mymusic.modules.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mymusic.util.SpacerComposable
import com.mymusic.util.TextFieldButtonComposable

@Composable
fun AccountComposable(
    viewModel: AccountViewModel = viewModel()
) {
    viewModel.user.observeAsState().value?.data?.let { user ->
        with(user) {
            View(name, email!!, gender, dob)
        }
    }
}

@Composable
private fun View(name: String, email: String, gender: String, dob: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldButtonComposable(
            icon = Icons.Default.Person,
            title = "Name",
            value = name
        )
        SpacerComposable(20.dp)
        TextFieldButtonComposable(
            icon = Icons.Default.AlternateEmail,
            title = "Email",
            value = email
        )
        SpacerComposable(20.dp)
        TextFieldButtonComposable(
            icon = Icons.Default.CalendarToday,
            title = "Date Of Birth",
            value = dob
        )
        SpacerComposable(20.dp)
        TextFieldButtonComposable(
            icon = when {
                gender.compareTo("Male", true) == 0 -> Icons.Default.Male
                gender.compareTo("Female", true) == 0 -> Icons.Default.Female
                else -> Icons.Default.Transgender
            },
            title = "Gender",
            value = gender
        )
    }
}