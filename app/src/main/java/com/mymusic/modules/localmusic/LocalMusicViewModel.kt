package com.mymusic.modules.localmusic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mymusic.model.Music
import com.mymusic.AppContainer
import kotlinx.coroutines.launch

class LocalMusicViewModel(
    private val localMusicRepository: LocalMusicRepository = AppContainer.localMusicRepository
) : ViewModel() {

    val musics = MutableLiveData<List<Music>>()

    fun retrieveMusic() {
        viewModelScope.launch {
            musics.value = localMusicRepository.getMusicList()
        }
    }
}