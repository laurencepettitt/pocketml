package com.example.pocketml.ui.viewmodel

import android.net.Uri
import android.text.BoringLayout
import androidx.lifecycle.*
import com.example.pocketml.DImageQuery
import com.example.pocketml.domain.usecases.GetDClasses
import com.example.pocketml.domain.usecases.GetDImageDetail
import com.example.pocketml.domain.usecases.SaveDImage
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getDImageDetail: GetDImageDetail,
    private val getDClasses: GetDClasses,
    private val saveDImage: SaveDImage
) : ViewModel() {

    private val _existingId = MutableLiveData<String>()
    val existingId: LiveData<String> = _existingId

    val existingDImage: LiveData<DImageQuery.DImage> = existingId.switchMap { id ->
        liveData {
            getDImageDetail(id).getOrNull()?.let { dImage ->
                emit(dImage)
            }
        }
    }

    private val localDImageUri = MutableLiveData<Uri>()

    val dImageUri = MediatorLiveData<Uri>()

    init {
        dImageUri.addSource(existingDImage) { dImage ->
            dImage?.url?.let {
                dImageUri.value = Uri.parse(it)
            }
        }

        dImageUri.addSource(localDImageUri) {
            it?.let {
                dImageUri.removeSource(existingDImage)
                dImageUri.setValue(it)
            }
        }
    }

    var dClasses: LiveData<List<String>> = existingId.switchMap {
        liveData {
            getDClasses().getOrNull()?.let { dClasses ->
                emit(dClasses)
            }
        }
    }

    private val _dClassInputText = MutableLiveData<String>()
    val dClassInputText: LiveData<String> = _dClassInputText

    val isDClassInputTextValid: LiveData<Boolean> =
        Transformations.map(dClassInputText) { textInput ->
            !textInput.isNullOrEmpty() && textInput.all { it.isLetter() }
        }

    val isDImageUriValid: LiveData<Boolean> = Transformations.map(dImageUri) { it != null }

    private val _isSaving = MutableLiveData<Boolean>()
    val isSaving: LiveData<Boolean> = _isSaving

    val isDataValid = MediatorLiveData<Boolean>()

    init {
        isDataValid.addSource(isDImageUriValid) {
            isDataValid.value = it && isDClassInputTextValid.value == true
        }
        isDataValid.addSource(isDClassInputTextValid) {
            isDataValid.value = it && isDImageUriValid.value == true
        }
    }

    private val _navigateToOverview = MutableLiveData<Boolean>()
    val navigateToOverview: LiveData<Boolean> = _navigateToOverview

    private val _makeSnackbar = MutableLiveData<String?>()
    val makeToast: LiveData<String?> = _makeSnackbar

    fun setLocalDImageUri(uri: Uri) {
        localDImageUri.value = uri
    }

    fun onSave() = viewModelScope.launch() {
        if (isDataValid.value != true) {
            _makeSnackbar.value =
                "Sorry, you can't save without setting an image and valid class name."
            return@launch
        }

        if (isSaving.value == true) {
            return@launch
        }

        _isSaving.value = true

        val dImage = existingDImage.value

        val result = saveDImage(
            dClass = dClassInputText.value,
            id = dImage?.id,
            version = dImage?.version,
            localUri = localDImageUri.value
        )

        result.exceptionOrNull()?.let {
            _makeSnackbar.value = "Failed to save."
        }

        _isSaving.value = false

        result.onSuccess {
            onNavigateToOverview()
        }
    }

    fun setId(id: String?) {
        id?.let {
            _existingId.value = id
        }
    }

    fun onReset() {
//        _existingId.value = null
//        localDImageUri.value = null
    }

    fun onNavigateToOverview() {
        _navigateToOverview.value = true
    }

    fun doneNavigateToOverview() {
        _navigateToOverview.value = false
        onReset()
    }

    fun setDClassInputText(dClassText: String) {
        _dClassInputText.value = dClassText
    }

    fun doneMakingSnackbar() {
        _makeSnackbar.value = null
    }
}
