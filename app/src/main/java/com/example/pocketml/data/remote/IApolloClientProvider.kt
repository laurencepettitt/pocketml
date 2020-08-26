package com.example.pocketml.data.remote

import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query

interface IApolloClientProvider {
    suspend fun <D : Operation.Data, T, V : Operation.Variables> query(query: Query<D, T, V>): Result<T>
    suspend fun <D : Operation.Data, T, V : Operation.Variables> mutate(mutation: Mutation<D, T, V>): Result<T>
}