package com.example.pocketml.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketml.DImagesQuery
import com.example.pocketml.domain.usecases.GetDImagesList
import kotlinx.coroutines.launch
import timber.log.Timber

class OverviewViewModel(
    private val getDImagesList: GetDImagesList
) : ViewModel() {

    private var _selectedDImage = MutableLiveData<String?>()
    val selectedDImage: LiveData<String?> = _selectedDImage

    private val _dImages = MutableLiveData<List<DImagesQuery.DImage>>()
    val dImages: LiveData<List<DImagesQuery.DImage>> = _dImages

    private val _isDImagesLoading = MutableLiveData<Boolean>()
    val isDImageLoading: LiveData<Boolean> = _isDImagesLoading

    fun loadDImages() = viewModelScope.launch {
        _isDImagesLoading.value = true
        getDImagesList()
            .onSuccess {
                this@OverviewViewModel._dImages.value = it
            }
            .onFailure {
                Timber.e("Failed to fetch dImages: $it")
            }
        _isDImagesLoading.value = false
    }

    fun onDImageClicked(dImage: DImagesQuery.DImage) {
        this._selectedDImage.value = dImage.id
    }

    fun doneDImageDetailNavigated() {
        this._selectedDImage.value = null
    }
}
