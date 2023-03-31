package com.example.rit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rit.domain.RequestResult
import com.example.rit.domain.entity.CountryNameProbability
import com.example.rit.domain.repo.IHomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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

    private val _customResponseStateFlow = MutableStateFlow<String?>(null)
    val customResponseStateFlow = _customResponseStateFlow.asStateFlow()

    private val _errorSharedFlow = MutableSharedFlow<String>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    private val isLoading = MutableSharedFlow<Boolean>()
    val loadingSharedFlow = isLoading.asSharedFlow()

    fun sendCustomRequest(url: String) {
        viewModelScope.launch {
            isLoading.emit(true)
            when (val response = repo.sendCustomRequest(url)) {
                is RequestResult.Success -> _customResponseStateFlow.emit(response.result)
                is RequestResult.Error -> {
                    _customResponseStateFlow.emit(response.data ?: "Unknown error")
                    _errorSharedFlow.emit(response.data ?: "Unknown error")
                }
            }
            isLoading.emit(false)
        }
    }

    suspend fun getNameInCountryProbability(name: String) {
        isLoading.emit(true)
        val slices = name.split(',')
        when (val response = repo.getNameInfo(slices)) {
            is RequestResult.Success -> _nameCountryStateFlow.emit(response.result)
            is RequestResult.Error -> _errorSharedFlow.emit(response.data ?: "Unknown error")
        }
        isLoading.emit(false)
    }

    fun getImage() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.emit(true)
            when (val response = repo.getDogImage()) {
                is RequestResult.Success -> _imageUrlStateFlow.emit(response.result)
                is RequestResult.Error -> _errorSharedFlow.emit(response.data ?: "Unknown error")
            }
            isLoading.emit(false)
        }
    }
}