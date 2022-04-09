package com.mymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.model.Music
import com.mymusic.app.AppContainer
import com.mymusic.repository.LocalStorageRepository
import kotlinx.coroutines.launch

class LocalStorageVM(
    private val localStorageRepository: LocalStorageRepository = AppContainer.storageRepository
) : ViewModel() {

    val musics = MutableLiveData<List<Music>>()

    fun retrieveMusic() {
        viewModelScope.launch {
            musics.value = localStorageRepository.getMusicList()
        }
    }
}