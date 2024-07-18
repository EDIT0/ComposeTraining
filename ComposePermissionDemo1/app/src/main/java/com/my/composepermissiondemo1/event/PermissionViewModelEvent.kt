package com.my.composepermissiondemo1.event

sealed interface PermissionViewModelEvent {
    class RequestPermission() : PermissionViewModelEvent
}