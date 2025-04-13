package com.task.assetratesapp.presentation.assetList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.assetratesapp.domain.core.usecase.AssetUseCases
import com.task.assetratesapp.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetViewModel @Inject constructor(
    private val useCases: AssetUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AssetListState(isLoading = true))
    val state: StateFlow<AssetListState> = _state

    init {
        handleIntent(AssetListIntent.LoadAssets)
        startAutoRefresh()
    }

    fun handleIntent(intent: AssetListIntent) {
        when (intent) {
            is AssetListIntent.LoadAssets -> viewModelScope.launch { loadAssets() }
            is AssetListIntent.AddAsset -> viewModelScope.launch {
                useCases.addAssetUseCase(intent.assetCode)
                loadAssets()
            }
            is AssetListIntent.RemoveAsset -> viewModelScope.launch {
                useCases.removeAssetUseCase(intent.assetCode)
                loadAssets()
            }
            is AssetListIntent.RefreshRates -> viewModelScope.launch { refreshRates() }
        }
    }

    private suspend fun loadAssets() {
        useCases.fetchAssetsListUseCase.execute().collect { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    val assetPresentations = resource.data?.map { it } ?: emptyList()
                    _state.value = AssetListState(
                        isLoading = false,
                        assets = assetPresentations,
                        error = null
                    )
                }
                Status.ERROR -> {
                    _state.value = AssetListState(
                        isLoading = false,
                        assets = emptyList(),
                        error = resource.message
                    )
                }
                Status.LOADING -> {
                    _state.value = AssetListState(isLoading = true)
                }
            }
        }
    }

    private suspend fun refreshRates() {
        // Similar logic to loadAssets
        loadAssets()
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