package com.example.pocketml.domain.usecases

import com.example.pocketml.domain.repos.DatabaseRepo

class GetDClasses(private val databaseRepo: DatabaseRepo) {
    suspend operator fun invoke() = databaseRepo.getDClasses().mapCatching {
        it.dClasses
    }
}
