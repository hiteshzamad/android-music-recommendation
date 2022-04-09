package com.mymusic.initialize

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.app.AppContainer
import com.mymusic.model.DeviceState
import com.mymusic.model.Task
import com.mymusic.repository.AccountRepository
import kotlinx.coroutines.launch

class InitializeViewModel(
    private val accountRepository: AccountRepository = AppContainer.accountRepository
) : ViewModel() {

    val state = MutableLiveData<Task<DeviceState>>(Task.Init())

    init {
        viewModelScope.launch {
            state.value = Task.Running()
            try {
                state.value = Task.Success(accountRepository.getState())
            } catch (e: Exception) {
                var message = "Error"
                e.message?.let { message = it }
                state.value = Task.Failed(message)
            }
        }
    }
}