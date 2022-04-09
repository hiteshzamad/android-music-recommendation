package com.mymusic.modules.account

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Task
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val accountRepository: AccountRepository = AppContainer.accountRepository
) : ViewModel() {
    val signUp = MutableLiveData<Task<Unit>>(Task.Init())

    fun signUp(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            signUp.value = Task.Running()
            if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()
            ) {
                signUp.value = Task.Failed("Invalid Email")
                return@launch
            }
            if (password.isEmpty() || password.length < 8) {
                signUp.value = Task.Failed("Passwords must be at least 8 characters in length")
                return@launch
            }
            if (confirmPassword.isEmpty() || confirmPassword.length < 8 || password != confirmPassword) {
                signUp.value = Task.Failed("Passwords do not match")
                return@launch
            }
            try {
                accountRepository.signUp(email, password)
                signUp.value = Task.Success()
            } catch (e: Exception) {
                var message = "Error"
                e.message?.let { message = it }
                signUp.value = Task.Failed(message = message)
                return@launch
            }
        }
    }
}