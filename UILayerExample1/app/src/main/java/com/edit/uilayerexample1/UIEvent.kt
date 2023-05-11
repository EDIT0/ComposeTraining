package com.edit.uilayerexample1

sealed class UIEvent {
    data class ShowMessage(
        val message: String
    ): UIEvent()

}
