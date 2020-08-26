package com.example.pocketml

import com.example.pocketml.data.remote.IApolloClientProvider
import com.example.pocketml.data.remote.ApolloClientProviderImpl
import com.example.pocketml.ui.viewmodel.OverviewViewModel
import com.example.pocketml.domain.repos.DatabaseRepo
import com.example.pocketml.domain.repos.DatasetRepo
import com.example.pocketml.domain.repos.StorageRepo
import com.example.pocketml.domain.usecases.*
import com.example.pocketml.ui.viewmodel.DetailViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val diModule = module {
    single<IApolloClientProvider> { ApolloClientProviderImpl() }

    // repos
    single { DatabaseRepo(get()) }
    single { StorageRepo(Firebase.storage("gs://pocketml.appspot.com")) }
    single { DatasetRepo(get(), get()) }

    // usecases
    single { GetDImageDetail(get()) }
    single { GetDImagesList(get()) }
    single { GetDClasses(get()) }
    single { saveDImage(get()) }

    viewModel { OverviewViewModel(get()) }
    viewModel { DetailViewModel(get(), get(), get()) }
}
