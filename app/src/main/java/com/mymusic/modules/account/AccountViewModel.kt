package com.mymusic.modules.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.model.Resource
import com.mymusic.model.User
import kotlinx.coroutines.launch

class AccountViewModel(
    private val accountRepository: AccountRepository = AppContainer.accountRepository
) : ViewModel() {
    val user = MutableLiveData<Resource<User>>(Resource())

    init {
        viewModelScope.launch {
            user.value = Resource(accountRepository.getUser())
        }
    }
}