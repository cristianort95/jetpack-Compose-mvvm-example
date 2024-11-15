package com.example.myapplication.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Character
import com.example.myapplication.model.repository.ApiRepository
import kotlinx.coroutines.launch

open class CharacterViewModel: ViewModel() {
    private var page: Int? = 1
    private val characterRepository: ApiRepository = ApiRepository()

    private val _characterData = MutableLiveData<List<Character>>()
    private val _isLoading = MutableLiveData(false)

    val characterData = _characterData
    val isLoading: LiveData<Boolean> = _isLoading


    fun getCharacterData() {
        if (page == null) return
        if (_isLoading.value == true) return


        viewModelScope.launch {
            _isLoading.postValue(true)
            val characterResult = characterRepository.getCharacter(page ?: 1)
            if (characterResult.success && characterResult.data != null) {
                val updatedList = _characterData.value.orEmpty() + characterResult.data
                _characterData.postValue(updatedList)
            } else {
                Log.e("TEST", "fetch Character failed: ${characterResult.message}")
            }
            page = if (characterResult.info?.next != null || characterResult.statusCode != 404) page?.plus(1) else null
            _isLoading.postValue(false)
        }
    }
}