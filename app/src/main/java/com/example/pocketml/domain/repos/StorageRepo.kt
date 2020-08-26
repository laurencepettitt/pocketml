package com.example.pocketml.domain.repos

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class StorageRepo(
    private val storage: FirebaseStorage
) {
    private fun getDImageRef(id: String, version: Int): StorageReference =
        storage.reference.child("$id-$version.jpg")

    /**
     * Uploads a new DImage into storage.
     * This will overwrite any existing image with the same id and version number.
     */
    suspend fun uploadDImage(id: String, version: Int, localUri: Uri): Result<Uri> {
        val dImageRef = getDImageRef(id, version)
        return try {
            dImageRef.putFile(localUri).await()
            Timber.d("SHEN: putFile finished")
            Result.success(dImageRef.downloadUrl.await()) // TODO: construct url manually?
        } catch (e: Exception) {
            Timber.d("SHEN: putFile failed")
            Result.failure(e)
        }
    }
}
