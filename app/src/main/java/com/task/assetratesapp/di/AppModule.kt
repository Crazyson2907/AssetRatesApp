package com.task.assetratesapp.di

import android.content.Context
import androidx.room.Room
import com.task.assetratesapp.data.cache.AssetDatabase
import com.task.assetratesapp.data.network.AssetRepositoryImpl
import com.task.assetratesapp.data.network.ApiKeyInterceptor
import com.task.assetratesapp.domain.cache.core.AssetRepository
import com.task.assetratesapp.domain.cache.usecase.GetAssetsFromCacheUseCase
import com.task.assetratesapp.domain.core.usecase.AddAssetUseCase
import com.task.assetratesapp.domain.core.usecase.AssetUseCases
import com.task.assetratesapp.domain.core.usecase.FetchAssetsListUseCase
import com.task.assetratesapp.domain.core.usecase.InitializeAssetsUseCase
import com.task.assetratesapp.domain.core.usecase.RemoveAssetUseCase
import com.task.assetratesapp.domain.network.core.ExchangeRatesApiService
import com.task.assetratesapp.domain.network.usecase.FetchAssetsFromApiUseCase
import com.task.assetratesapp.domain.network.usecase.GetSupportedCurrenciesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.exchangerate.host/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideExchangeRateApi(retrofit: Retrofit): ExchangeRatesApiService =
        retrofit.create(ExchangeRatesApiService::class.java)

    @Provides
    @Singleton
    fun provideAssetDatabase(@ApplicationContext appContext: Context): AssetDatabase =
        Room.databaseBuilder(appContext, AssetDatabase::class.java, "asset_database").build()

    @Provides
    @Singleton
    fun provideAssetRepository(
        api: ExchangeRatesApiService,
        database: AssetDatabase
    ): AssetRepository = AssetRepositoryImpl(api, database)

    @Provides
    @Singleton
    fun provideAssetUseCases(repository: AssetRepository, @ApplicationContext appContext: Context): AssetUseCases = AssetUseCases(
        fetchAssetsListUseCase = FetchAssetsListUseCase.Base(
            fetchAssetsFromApiUseCase = FetchAssetsFromApiUseCase(repository),
            getAssetsFromCacheUseCase = GetAssetsFromCacheUseCase(repository)
        ),
        addAssetUseCase = AddAssetUseCase(repository),
        removeAssetUseCase = RemoveAssetUseCase(repository),
        getAssetsFromCacheUseCase = GetAssetsFromCacheUseCase(repository),
        fetchAssetsFromApiUseCase = FetchAssetsFromApiUseCase(repository),
        initializeAssetsUseCase = InitializeAssetsUseCase(repository, appContext),
        getSupportedCurrenciesUseCase = GetSupportedCurrenciesUseCase(repository)
    )
}