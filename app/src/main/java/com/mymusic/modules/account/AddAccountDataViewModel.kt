package com.mymusic.modules.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Task
import kotlinx.coroutines.launch

class AddAccountDataViewModel(
    private val accountRepository: AccountRepository = AppContainer.accountRepository
) : ViewModel() {

    val addDetail = MutableLiveData<Task<Unit>>()
    fun addDetail(name: String, gender: String, dob: String) {
        viewModelScope.launch {
            addDetail.value = Task.Running()
            if (name.isEmpty()) {
                addDetail.value = Task.Failed("Name is Empty")
                return@launch
            }
            if (dob.isEmpty()) {
                addDetail.value = Task.Failed("Date is Empty")
                return@launch
            }
            try {
                accountRepository.addDetail(name, gender, dob)
                addDetail.value = Task.Success()
            } catch (e: Exception) {
                var message = "Error"
                e.message?.let { message = it }
                addDetail.value = Task.Failed(message = message)
                return@launch
            }
        }
    }
}