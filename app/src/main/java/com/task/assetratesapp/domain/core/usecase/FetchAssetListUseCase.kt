package com.task.assetratesapp.domain.core.usecase

import com.task.assetratesapp.domain.cache.usecase.GetAssetsFromCacheUseCase
import com.task.assetratesapp.domain.core.model.Asset
import com.task.assetratesapp.domain.network.usecase.FetchAssetsFromApiUseCase
import com.task.assetratesapp.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface FetchAssetsListUseCase {
    suspend fun execute(): Flow<Response<List<Asset>>>

    class Base @Inject constructor(
        private val fetchAssetsFromApiUseCase: FetchAssetsFromApiUseCase,
        private val getAssetsFromCacheUseCase: GetAssetsFromCacheUseCase
    ) : FetchAssetsListUseCase {
        override suspend fun execute(): Flow<Response<List<Asset>>> = flow {
            emit(Response.loading(null))

            val codes = getAssetsFromCacheUseCase.execute()
            if (codes.isEmpty()) {
                emit(Response.success(emptyList()))
            } else {
                // always fetch from API
                fetchAssetsFromApiUseCase.execute().collect { apiResource ->
                    emit(apiResource)
                }
            }
        }
    }
}