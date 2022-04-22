package com.mymusic.modules.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mymusic.util.SpacerComposable

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
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 20.dp)
            ) {
                Text(
                    text = name,
                    color = MaterialTheme.colors.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                SpacerComposable(i = 3.dp)
                Text(
                    text = email,
                    color = MaterialTheme.colors.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}