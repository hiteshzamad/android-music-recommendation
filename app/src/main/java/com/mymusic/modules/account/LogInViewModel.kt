package com.mymusic.modules.account

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Task
import kotlinx.coroutines.launch

class LogInViewModel(
    private val accountRepository: AccountRepository = AppContainer.accountRepository
) : ViewModel() {

    val logIn = MutableLiveData<Task<Unit>>(Task.Init())

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            logIn.value = Task.Running()
            if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()
            ) {
                logIn.value = Task.Failed("Invalid Email")
                return@launch
            }
            if (password.isEmpty() || password.length < 8) {
                logIn.value = Task.Failed("Passwords must be at least 8 characters in length")
                return@launch
            }
            try {
                accountRepository.logIn(email, password)
                logIn.value = Task.Success()
            } catch (e: Exception) {
                var message = "Error"
                e.message?.let { message = it }
                logIn.value = Task.Failed(message = message)
                return@launch
            }
        }
    }
}