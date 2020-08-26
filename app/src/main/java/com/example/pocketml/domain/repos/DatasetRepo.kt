package com.example.pocketml.domain.repos

import android.net.Uri
import com.example.pocketml.isLocal

class DatasetRepo(
    private val storageRepo: StorageRepo,
    private val databaseRepo: DatabaseRepo
) {

    suspend fun newDImage(dClass: String, localUri: Uri): Result<Unit> {
        if (!localUri.isLocal()) {
            return Result.failure(Exception("Uri must be local."))
        }

        if (dClass == "") {
            return Result.failure(Exception("DClass must not be empty."))
        }

        val version = 0
        val id = createNewId()
        return storageRepo.uploadDImage(id, version, localUri)
            .mapCatching { cloudUri ->
                return@mapCatching databaseRepo.newDImage(dClass, id, version, cloudUri.toString())
                    .getOrThrow()
            }
    }

    suspend fun updateDImage(dClass: String?, id: String, uri: Uri?, version: Int?): Result<Unit> {
        val safeDClass = if (dClass != "") dClass else null

        // Branch if we need to update the image (and potentially it's class)
        if (uri != null && uri.isLocal()) {
            if (version == null) {
                return Result.failure(Exception("To update DImage url a version number must be given."))
            }
            val newVersion = version + 1
            return storageRepo.uploadDImage(id, newVersion, uri)
                .mapCatching { cloudUri ->
                    return@mapCatching databaseRepo.updateDImage(
                        dClass = safeDClass,
                        id = id,
                        version = newVersion,
                        url = cloudUri.toString()
                    ).getOrThrow()
                }
        }

        // Branch if we only need to update the DClass (and not the DImage)
        if (safeDClass != null) {
            return databaseRepo.updateDImage(
                dClass = safeDClass,
                id = id
            )
        }

        // If we got here we didn't actually need to update anything
        return Result.success(Unit)
    }

    private fun createNewId(): String = (1..ID_LENGTH)
        .map { kotlin.random.Random.nextInt(0, CHAR_POOL.size) }
        .map(CHAR_POOL::get)
        .joinToString("")

    private val ID_LENGTH = 15
    private val CHAR_POOL: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
}
