package com.mymusic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.model.Music
import com.mymusic.AppContainer
import com.mymusic.repository.StorageRepository
import kotlinx.coroutines.launch

class StorageViewModel(
    private val storageRepository: StorageRepository = AppContainer.storageRepository
) : ViewModel() {

    val musics = MutableLiveData<List<Music>>()

    fun retrieveMusic() {
        viewModelScope.launch {
            musics.value = storageRepository.getMusicList()
        }
    }
}