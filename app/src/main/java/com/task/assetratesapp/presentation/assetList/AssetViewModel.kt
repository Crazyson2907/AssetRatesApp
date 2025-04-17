package com.task.assetratesapp.presentation.assetList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.assetratesapp.domain.core.usecase.AssetUseCases
import com.task.assetratesapp.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetViewModel @Inject constructor(
    private val useCases: AssetUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AssetListState(isLoading = true))
    val state: StateFlow<AssetListState> = _state

    private val _supportedCurrencies = MutableStateFlow<Map<String, String>>(emptyMap())
    val supportedCurrencies: StateFlow<Map<String, String>> = _supportedCurrencies

    init {
        viewModelScope.launch {
            useCases.initializeAssetsUseCase.execute()
            handleIntent(AssetListIntent.LoadAssets)
        }
        loadSupportedCurrencies()
        startAutoRefresh()
    }

    fun handleIntent(intent: AssetListIntent) {
        when (intent) {
            is AssetListIntent.LoadAssets -> viewModelScope.launch {
                loadAssets()
            }
            is AssetListIntent.AddAsset -> viewModelScope.launch {
                useCases.addAssetUseCase(intent.assetCode)
                loadAssets(showLoading = false)
            }
            is AssetListIntent.RemoveAsset -> viewModelScope.launch {
                useCases.removeAssetUseCase(intent.assetCode)
                loadAssets(showLoading = false)
            }
            is AssetListIntent.RefreshRates -> viewModelScope.launch {
                refreshRates()
            }
        }
    }

    private suspend fun loadAssets(showLoading: Boolean = true) {
        if (showLoading) {
            _state.update { it.copy(isLoading = true, error = null) }
        }

        useCases.fetchAssetsListUseCase.execute().collect { result ->
            _state.update {
                when (result.status) {
                    Status.SUCCESS -> it.copy(
                        assets = result.data.orEmpty(),
                        isLoading = false,
                        error = null
                    )

                    Status.ERROR -> it.copy(
                        error = result.message ?: "Something went wrong",
                        isLoading = false
                    )

                    Status.LOADING -> if (showLoading) it.copy(isLoading = true) else it
                }
            }
        }
    }

    private suspend fun refreshRates() {
        _state.update { it.copy(isRefreshing = true) }

        useCases.fetchAssetsListUseCase.execute().collect { result ->
            _state.update {
                when (result.status) {
                    Status.SUCCESS -> it.copy(
                        assets = result.data.orEmpty(),
                        isRefreshing = false
                    )

                    Status.ERROR -> it.copy(
                        error = result.message,
                        isRefreshing = false
                    )

                    else -> it
                }
            }
        }
    }

    fun loadSupportedCurrencies() {
        viewModelScope.launch {
            val result = useCases.getSupportedCurrenciesUseCase.execute()
            _supportedCurrencies.value = result
        }
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (isActive) {
                delay(3000L)
                handleIntent(AssetListIntent.RefreshRates)
            }
        }
    }
}