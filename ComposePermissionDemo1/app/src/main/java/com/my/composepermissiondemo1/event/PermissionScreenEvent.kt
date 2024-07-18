package com.my.composepermissiondemo1.event

sealed interface PermissionScreenEvent {
    class ShowPermissionDialog(val message: String, val buttonText: String) : PermissionScreenEvent
    class MoveToLoginScreen() : PermissionScreenEvent
}