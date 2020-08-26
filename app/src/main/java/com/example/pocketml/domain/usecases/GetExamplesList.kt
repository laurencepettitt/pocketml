package com.example.pocketml.domain.usecases

import com.example.pocketml.DImagesQuery
import com.example.pocketml.domain.repos.DatabaseRepo


class GetDImagesList(private val databaseRepo: DatabaseRepo) {
    suspend operator fun invoke(): Result<List<DImagesQuery.DImage>> =
        databaseRepo.getDImages().mapCatching {
            it.dImages
        }
}