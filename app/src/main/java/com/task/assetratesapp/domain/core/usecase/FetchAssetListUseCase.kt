package com.task.assetratesapp.domain.core.usecase

import com.task.assetratesapp.domain.cache.usecase.GetAssetsFromCacheUseCase
import com.task.assetratesapp.domain.core.model.Asset
import com.task.assetratesapp.domain.network.usecase.FetchAssetsFromApiUseCase
import com.task.assetratesapp.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface FetchAssetsListUseCase {
    suspend fun execute(): Flow<Response<List<Asset>>>

    class Base(
        private val fetchAssetsFromApiUseCase: FetchAssetsFromApiUseCase,
        private val getAssetsFromCacheUseCase: GetAssetsFromCacheUseCase
    ) : FetchAssetsListUseCase {
        override suspend fun execute(): Flow<Response<List<Asset>>> {
            val cachedAssets = getAssetsFromCacheUseCase.execute()
            return if (cachedAssets.isEmpty()) {
                // If cache is empty, get fresh data from API.
                fetchAssetsFromApiUseCase.execute()
            } else {
                // Otherwise, simply emit the cached data wrapped in a success Resource.
                flow { emit(Response.success(cachedAssets)) }
            }
        }
    }
}