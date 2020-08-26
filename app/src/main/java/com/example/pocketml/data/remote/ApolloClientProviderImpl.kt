package com.example.pocketml.data.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApolloClientProviderImpl : IApolloClientProvider {

    @Volatile
    private var apolloClient: ApolloClient? = null

    override suspend fun <D : Operation.Data, T, V : Operation.Variables> query(
        query: Query<D, T, V>
    ): Result<T> = withContext(Dispatchers.IO) {
        return@withContext try {
            getClient().query(query).toDeferred().await().toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun <D : Operation.Data, T, V : Operation.Variables> mutate(
        mutation: Mutation<D, T, V>
    ): Result<T> = withContext(Dispatchers.IO) {
        return@withContext try {
            getClient().mutate(mutation).toDeferred().await().toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getClient(): ApolloClient {
        return apolloClient ?: synchronized(this) {
            apolloClient ?: build().also {
                apolloClient = it
            }
        }
    }

    private fun build(): ApolloClient =
        ApolloClient.builder()
            .serverUrl(SERVER_URL)
            .build()

    companion object {
        private const val SERVER_URL = "https://us-central1-pocketml.cloudfunctions.net/graphql"
    }
}

fun <T> Response<T>.toResult(): Result<T> {
    val data = this.data

    return when {
        !errors.isNullOrEmpty() -> {
            Result.failure(Exception(errors!!.first().message))
        }
        data == null -> {
            Result.failure(Exception("Response data is null"))
        }
        else -> {
            Result.success(data)
        }
    }
}
