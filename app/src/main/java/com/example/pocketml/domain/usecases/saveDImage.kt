package com.example.pocketml.domain.usecases

import android.net.Uri
import com.example.pocketml.domain.repos.DatasetRepo
import com.example.pocketml.isLocal

class saveDImage(
    private val datasetRepo: DatasetRepo
) {
    suspend operator fun invoke(
        dClass: String?,
        id: String?,
        version: Int?,
        uri: Uri?
    ): Result<Unit> {
        // localUri is a local uri or null
        val localUri = if (uri == null || uri.isLocal()) uri else null

        if (id == null) {
            if (localUri == null || dClass == null) {
                return Result.failure(Exception("You must provide a local Uri and DClass for a new DImage."))
            }
            return datasetRepo.newDImage(dClass, localUri)
        } else {
            return datasetRepo.updateDImage(dClass, id, localUri, version)
        }
    }
}
