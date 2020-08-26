package com.example.pocketml.domain.usecases

import com.example.pocketml.DImageQuery
import com.example.pocketml.domain.repos.DatabaseRepo
import kotlin.Exception

class GetDImageDetail(
    private val databaseRepo: DatabaseRepo
) {
    suspend operator fun invoke(id: String): Result<DImageQuery.DImage> =
        databaseRepo.getDImage(id).mapCatching {
            it.dImage ?: throw Exception("DImageQuery return null.")
        }
}