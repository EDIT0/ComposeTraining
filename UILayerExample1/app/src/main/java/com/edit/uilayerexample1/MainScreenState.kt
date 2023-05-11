package com.edit.uilayerexample1

data class MainScreenState(
    var isCountButtonVisible: Boolean = false,
    var displayingResult: String = "",
    var inputValue: String = ""
) {
}