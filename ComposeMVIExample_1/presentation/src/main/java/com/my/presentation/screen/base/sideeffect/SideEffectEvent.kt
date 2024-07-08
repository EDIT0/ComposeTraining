package com.my.presentation.screen.base.sideeffect

sealed interface SideEffectEvent {
    class Init : SideEffectEvent
    class ShowToast(val message: String) : SideEffectEvent
}