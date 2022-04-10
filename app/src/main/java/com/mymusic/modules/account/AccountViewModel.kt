package com.mymusic.modules.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.AppContainer
import com.mymusic.util.Resource
import kotlinx.coroutines.launch

class AccountViewModel(
    private val accountRepository: AccountRepository = AppContainer.accountRepository
) : ViewModel() {
    val user = MutableLiveData<Resource<Account>>(Resource())

    init {
        viewModelScope.launch {
            user.value = Resource(accountRepository.getUser())
        }
    }
}