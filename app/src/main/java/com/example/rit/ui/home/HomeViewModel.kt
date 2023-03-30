package com.example.rit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rit.domain.RequestResult
import com.example.rit.domain.entity.CountryNameProbability
import com.example.rit.domain.repo.IHomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: IHomeRepo,
) : ViewModel() {
    private val _nameCountryStateFlow = MutableStateFlow<List<CountryNameProbability>?>(null)
    val nameCountryStateFlow = _nameCountryStateFlow.asStateFlow()
    private val _imageUrlStateFlow = MutableStateFlow<String?>(null)
    val imageUrlStateFlow = _imageUrlStateFlow.asStateFlow()

    suspend fun getNameInCountryProbability(name: String) {
        val slices = name.split(',')
        when (val response = repo.getNameInfo(slices)) {
            is RequestResult.Success -> _nameCountryStateFlow.emit(response.result)
            is RequestResult.Error -> {}
        }
    }

    init {
        getImage()
    }

    fun getImage() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response =
                repo.sendCustomRequest("https://dog.ceo/api/breeds/image/random")) {
                is RequestResult.Success -> println(response.result)
                is RequestResult.Error -> {}
            }
            when (val response = repo.getDogImage()) {
                is RequestResult.Success -> _imageUrlStateFlow.emit(response.result)
                is RequestResult.Error -> {}
            }
        }
    }
}