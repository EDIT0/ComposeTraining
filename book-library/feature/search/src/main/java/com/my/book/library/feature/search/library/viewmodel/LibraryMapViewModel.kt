package com.my.book.library.feature.search.library.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqSearchBookHoldingLibrary
import com.my.book.library.core.common.util.LocationUtil
import com.my.book.library.domain.usecase.GetSearchBookHoldingLibraryUseCase
import com.my.book.library.domain.usecase.data_store.GetMyLibraryInfoUseCase
import com.my.book.library.feature.search.library.intent.LibraryMapUiEvent
import com.my.book.library.feature.search.library.intent.LibraryMapViewModelEvent
import com.my.book.library.feature.search.library.state.LibraryMapUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryMapViewModel @Inject constructor(
    private val app: Application,
    private val getSearchBookHoldingLibraryUseCase: GetSearchBookHoldingLibraryUseCase,
    private val getMyLibraryInfoUseCase: GetMyLibraryInfoUseCase,
    private val locationUtil: LocationUtil
): AndroidViewModel(application = app) {

    sealed interface SideEffectEvent {
        class ShowToast(val message: String): SideEffectEvent
        data object MoveToMyLocation : SideEffectEvent
        data object RequestLocationPermission : SideEffectEvent
    }

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    private val _libraryMapUiEvent = Channel<LibraryMapUiEvent>(capacity = Channel.UNLIMITED)
    val libraryMapUiState: StateFlow<LibraryMapUiState> = _libraryMapUiEvent.receiveAsFlow()
        .runningFold(
            initial = LibraryMapUiState(),
            operation = { state, event ->
                when(event) {
                    is LibraryMapUiEvent.UpdateDetailRegion -> {
                        state.copy(currentDetailRegion = event.detailRegion)
                    }
                    is LibraryMapUiEvent.UpdateUserLocation -> {
                        state.copy(userLatitude = event.latitude, userLongitude = event.longitude)
                    }
                    is LibraryMapUiEvent.UpdateHoldingLibraryList -> {
                        state.copy(holdingLibraryList = MutableStateFlow(value = event.holdingLibraryList!!))
                    }
                    is LibraryMapUiEvent.UpdateSelectedLibCode -> {
                        state.copy(selectedLibCode = event.libCode)
                    }
                    is LibraryMapUiEvent.UpdateSheetOffsetRatio -> {
                        state.copy(sheetOffsetRatio = event.ratio)
                    }
                }
            }
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, LibraryMapUiState())

    private var currentIsbn: String = ""
    private var locationUpdatesJob: Job? = null

    fun intentAction(libraryMapViewModelEvent: LibraryMapViewModelEvent) {
        when(libraryMapViewModelEvent) {
            is LibraryMapViewModelEvent.RequestInit -> {
                currentIsbn = libraryMapViewModelEvent.isbn
                viewModelScope.launch {
                    requestInit(isbn = libraryMapViewModelEvent.isbn)
                }
            }
            is LibraryMapViewModelEvent.UpdateRegion -> {
                viewModelScope.launch {
                    val detailRegion = libraryMapViewModelEvent.detailRegion
                    _libraryMapUiEvent.send(LibraryMapUiEvent.UpdateDetailRegion(detailRegion))
                    requestHoldingLibrary(
                        isbn = currentIsbn,
                        region = detailRegion.regionCode,
                        dtlRegion = detailRegion.code
                    )
                }
            }
            is LibraryMapViewModelEvent.SelectMarker -> {
                viewModelScope.launch {
                    _libraryMapUiEvent.send(LibraryMapUiEvent.UpdateSelectedLibCode(libraryMapViewModelEvent.libCode))
                }
            }
            is LibraryMapViewModelEvent.UpdateSheetOffsetRatio -> {
                viewModelScope.launch {
                    _libraryMapUiEvent.send(LibraryMapUiEvent.UpdateSheetOffsetRatio(libraryMapViewModelEvent.ratio))
                }
            }
            is LibraryMapViewModelEvent.StartLocationUpdates -> {
                startLocationUpdates()
            }
            is LibraryMapViewModelEvent.RequestMoveToMyLocation -> {
                viewModelScope.launch {
                    if (locationUtil.isLocationPermissionGranted()) {
                        _sideEffectEvent.send(SideEffectEvent.MoveToMyLocation)
                    } else {
                        _sideEffectEvent.send(SideEffectEvent.RequestLocationPermission)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationUpdatesJob?.cancel()
    }

    private fun startLocationUpdates() {
        locationUpdatesJob?.cancel()
        locationUpdatesJob = viewModelScope.launch {
            locationUtil.getLocationUpdates().collect { location ->
                _libraryMapUiEvent.send(
                    LibraryMapUiEvent.UpdateUserLocation(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                )
            }
        }
    }

    private suspend fun requestInit(isbn: String) {
        locationUtil.getLastKnownLocation()?.let { location ->
            _libraryMapUiEvent.send(
                LibraryMapUiEvent.UpdateUserLocation(latitude = location.latitude, longitude = location.longitude)
            )
        }
        if (locationUtil.isLocationPermissionGranted()) {
            startLocationUpdates()
        }
        getMyLibraryInfoUseCase.invoke().collect { result ->
            when (result) {
                is RequestResult.Success -> {
                    val detailRegion = result.resultData!!.detailRegion
                    _libraryMapUiEvent.send(LibraryMapUiEvent.UpdateDetailRegion(detailRegion))
                    requestHoldingLibrary(
                        isbn = isbn,
                        region = detailRegion.regionCode,
                        dtlRegion = detailRegion.code
                    )
                }
                is RequestResult.Error -> {
                    _sideEffectEvent.send(SideEffectEvent.ShowToast(message = result.message ?: ""))
                }
                else -> {}
            }
        }
    }

    private suspend fun requestHoldingLibrary(isbn: String, region: Int, dtlRegion: Int) {
        getSearchBookHoldingLibraryUseCase.invokePaging(
            reqSearchBookHoldingLibrary = ReqSearchBookHoldingLibrary(
                isbn = isbn,
                region = region,
                dtlRegion = dtlRegion,
                pageNo = 0,
                pageSize = 0
            )
        )
            .map { it ->
                it.map {
                    LogUtil.d_dev("소장 도서관 페이징 데이터: ${it}")
                    it
                }
            }
            .cachedIn(viewModelScope)
            .catch { e ->
                _sideEffectEvent.send(SideEffectEvent.ShowToast(message = e.message ?: ""))
            }
            .collect {
                _libraryMapUiEvent.send(LibraryMapUiEvent.UpdateHoldingLibraryList(holdingLibraryList = it))
            }
    }


}
