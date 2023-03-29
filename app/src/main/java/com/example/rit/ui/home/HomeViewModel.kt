package com.example.rit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rit.domain.RequestResult
import com.example.rit.domain.entity.CountryNameProbability
import com.example.rit.domain.repo.IHomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: IHomeRepo,
) : ViewModel() {
    private val _nameCountryStateFlow = MutableStateFlow<CountryNameProbability?>(null)
    val nameCountryStateFlow = _nameCountryStateFlow.asStateFlow()

    init {
        getNameInCountryProbability("Michael")
    }

    fun getNameInCountryProbability(name: String) {
        viewModelScope.launch {
            when (val response = repo.getNameInfo(name)) {
                is RequestResult.Success -> _nameCountryStateFlow.value = response.result
                is RequestResult.Error -> TODO()
            }
        }
    }
}