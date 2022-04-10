package com.mymusic.modules.account

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.util.Task
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val accountRepository: AccountRepository = AppContainer.accountRepository
) : ViewModel() {
    val forgot = MutableLiveData<Task<Unit>>()
    fun forgotPassword(email: String) {
        viewModelScope.launch {
            forgot.value = Task.Running()
            if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()
            ) {
                forgot.value = Task.Failed("Invalid Email")
                return@launch
            }
            try {
                accountRepository.forgotPassword(email)
                forgot.value = Task.Success()
            } catch (e: Exception) {
                var message = "Error"
                e.message?.let { message = it }
                forgot.value = Task.Failed(message = message)
                return@launch
            }
        }
    }
}