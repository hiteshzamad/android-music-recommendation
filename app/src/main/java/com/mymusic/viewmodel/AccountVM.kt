package com.mymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.app.AppContainer
import com.mymusic.model.Resource
import com.mymusic.model.User
import com.mymusic.repository.AccountRepository
import kotlinx.coroutines.launch

class AccountVM(
    private val accountRepository: AccountRepository = AppContainer.accountRepository
) : ViewModel() {
    val user = MutableLiveData<Resource<User>>(Resource())

    init {
        viewModelScope.launch {
            user.value = Resource(accountRepository.getUser())
        }
    }
}