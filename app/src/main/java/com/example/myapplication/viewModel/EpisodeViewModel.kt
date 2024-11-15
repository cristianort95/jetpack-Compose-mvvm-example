package com.example.myapplication.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Episode
import com.example.myapplication.model.repository.ApiRepository
import kotlinx.coroutines.launch

open class EpisodeViewModel: ViewModel() {
    private var page: Int? = 1
    private val episodeRepository: ApiRepository = ApiRepository()

    private val _episodeData = MutableLiveData<List<Episode>>()
    private val _isLoading = MutableLiveData(false)

    val episodeData = _episodeData
    val isLoading: LiveData<Boolean> = _isLoading


    fun getEpisodeData() {
        if (page == null) return
        if (_isLoading.value == true) return

        viewModelScope.launch {
            _isLoading.postValue(true)
            val episodeResult = episodeRepository.getEpisode(page ?: 1)
            if (episodeResult.success && episodeResult.data != null) {
                val updatedList = _episodeData.value.orEmpty() + episodeResult.data
                _episodeData.postValue(updatedList)
            } else {
                Log.e("TEST", "fetch Character failed: ${episodeResult.message}")
            }
            page = if (episodeResult.info?.next != null || episodeResult.statusCode != 404) page?.plus(1) else null
            _isLoading.postValue(false)
        }
    }
}