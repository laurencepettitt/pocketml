package com.example.pocketml.domain.repos

import com.apollographql.apollo.api.Input
import com.example.pocketml.*
import com.example.pocketml.data.remote.IApolloClientProvider

class DatabaseRepo(private val apolloClient: IApolloClientProvider) {

    suspend fun getDImage(id: String) = apolloClient.query(DImageQuery(id))

    suspend fun getDImages() = apolloClient.query(DImagesQuery())

    suspend fun getDClasses() = apolloClient.query(DClassesQuery())

    /**
     * Creates a new DImage in the database
     */
    suspend fun newDImage(dClass: String, id: String, version: Int, url: String): Result<Unit> =
        apolloClient.mutate(
            NewDImageMutation(
                dClass = dClass,
                id = id,
                version = version,
                url = url
            )
        ).mapCatching {
            if (it.setDImage != true) {
                throw Exception("NewDImageMutation failed")
            }
        }

    /**
     * Updates a DImage in the database.
     * If any of the nullable types are null, those fields will not be affected in the database.
     */
    suspend fun updateDImage(
        dClass: String? = null,
        id: String,
        version: Int? = null,
        url: String? = null
    ): Result<Unit> {
        return apolloClient.mutate(
            UpdateDImageMutation(
                dClass = Input.fromNullable(dClass),
                id = id,
                version = Input.fromNullable(version),
                url = Input.fromNullable(url)
            )
        ).mapCatching {
            if (it.updateDImage != true) {
                throw Exception("UpdateDImageMutation failed")
            }
        }
    }
}
