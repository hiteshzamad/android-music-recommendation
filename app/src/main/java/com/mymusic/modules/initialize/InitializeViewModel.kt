package com.mymusic.modules.initialize

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Task
import kotlinx.coroutines.launch

class InitializeViewModel(
    private val initializeRepository: InitializeRepository = AppContainer.initializeRepository
) : ViewModel() {

    val state = MutableLiveData<Task<DeviceState>>(Task.Init())

    init {
        viewModelScope.launch {
            state.value = Task.Running()
            try {
                state.value = Task.Success(initializeRepository.getState())
            } catch (e: Exception) {
                var message = "Error"
                e.message?.let { message = it }
                state.value = Task.Failed(message)
            }
        }
    }
}