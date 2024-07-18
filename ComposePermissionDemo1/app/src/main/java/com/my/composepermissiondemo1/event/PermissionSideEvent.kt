package com.my.composepermissiondemo1.event

sealed interface PermissionSideEvent {
    class Init() : PermissionSideEvent
    class RequestPermission() : PermissionSideEvent
}