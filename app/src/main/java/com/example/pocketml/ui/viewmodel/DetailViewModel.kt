package com.example.pocketml.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.example.pocketml.DImageQuery
import com.example.pocketml.domain.usecases.GetDClasses
import com.example.pocketml.domain.usecases.GetDImageDetail
import com.example.pocketml.domain.usecases.saveDImage
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailViewModel(
    private val getDImageDetail: GetDImageDetail,
    private val getDClasses: GetDClasses,
    private val saveDImage: saveDImage
) : ViewModel() {

    private val _existingId = MutableLiveData<String?>()
    val existingId: LiveData<String?> = _existingId

    val existingDImage: LiveData<DImageQuery.DImage?> = existingId.switchMap {
        liveData {
            emit(it?.let { getDImageDetail(it).getOrNull() })
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

    var dClasses: LiveData<List<String>?> = existingId.switchMap {
        liveData {
            emit(getDClasses().getOrNull())
        }
    }

    private val _dClassInputText = MutableLiveData<String>()
    val dClassInputText: LiveData<String> = _dClassInputText

    val isDClassInputTextValid: LiveData<Boolean> =
        Transformations.map(dClassInputText) { textInput ->
            !textInput.isNullOrEmpty() && textInput.all { it.isLetter() }
        }

    val isDImageUriValid: LiveData<Boolean> = Transformations.map(dImageUri) { it != null }

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

    private val _makeToast = MutableLiveData<String?>()
    val makeToast: LiveData<String?> = _makeToast

    fun setLocalDImageUri(uri: Uri) {
        localDImageUri.value = uri
    }

    fun onSave() = viewModelScope.launch() {
        if (isDataValid.value != true) {
            _makeToast.value =
                "Sorry, you can't save without setting an image and valid class name."
            return@launch
        }

        val dImage = existingDImage.value

        val result = saveDImage(
            dClass = dClassInputText.value,
            id = dImage?.id,
            version = dImage?.version,
            uri = dImageUri.value
        )
        result.exceptionOrNull()?.let {
            Timber.d(it)
        }

        onNavigateToOverview()
    }

    fun setId(id: String?) {
        _existingId.value = id
    }

    fun onReset() {
        _existingId.value = null
        localDImageUri.value = null
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

    fun doneMakingToast() {
        _makeToast.value = null
    }
}
