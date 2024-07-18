package com.my.composepermissiondemo1.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.composepermissiondemo1.event.PermissionSideEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OneViewModel @Inject constructor(

) : ViewModel() {

    private val _permissionSideEvent = Channel<PermissionSideEvent>()
    val permissionSideEvent = _permissionSideEvent.receiveAsFlow()

    private var isAlreadyRequest = mutableStateOf(true)

    fun getIsAlreadyRequest(): Boolean {
        return isAlreadyRequest.value
    }
    fun setIsAlreadyRequest(value: Boolean) {
        isAlreadyRequest.value = value
    }

    fun requestPermission() {
        viewModelScope.launch {
            setIsAlreadyRequest(true)
            _permissionSideEvent.send(PermissionSideEvent.RequestPermission())
        }
    }

    override fun onCleared() {
        super.onCleared()
        _permissionSideEvent.close()
    }
}