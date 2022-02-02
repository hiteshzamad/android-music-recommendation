package com.mymusic.composable

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mymusic.R
import com.mymusic.model.DeviceState
import com.mymusic.model.Task
import com.mymusic.viewmodel.InitializeViewModel
import kotlinx.coroutines.delay

@Composable
fun InitializeComposable(
    viewModel: InitializeViewModel = viewModel(),
    onInitialize: (DeviceState) -> Unit
) {
    var running = false
    val context = LocalContext.current
    viewModel.state.observeAsState().value?.let { task ->
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
                    delay(100)
                    task.data?.let { onInitialize(it) }
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "My Music",
            fontSize = 16.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(30.dp))
        ProgressBarComposable(enabled = running)
    }
}