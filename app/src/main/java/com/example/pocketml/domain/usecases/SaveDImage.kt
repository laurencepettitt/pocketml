package com.example.pocketml.domain.usecases

import android.net.Uri
import com.example.pocketml.domain.repos.DatasetRepo
import com.example.pocketml.isLocal

class SaveDImage(
    private val datasetRepo: DatasetRepo
) {
    suspend operator fun invoke(
        dClass: String?,
        id: String?,
        version: Int?,
        localUri: Uri?
    ): Result<Unit> {
        if (localUri != null && !localUri.isLocal()) {
            return Result.failure(Exception("Cannot save a non-local image"))
        }

        if (id == null) {
            if (localUri == null || dClass == null) {
                return Result.failure(Exception("You must provide a Uri and DClass for a new DImage."))
            }
            return datasetRepo.newDImage(dClass, localUri)
        } else {
            return datasetRepo.updateDImage(dClass, id, localUri, version)
        }
    }
}
