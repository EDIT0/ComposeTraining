package com.edit.uilayerexample1

sealed class CounterEvent{
    data class ValueEntered(
        val value1: String
    ): CounterEvent()

    object CountButtonClicked: CounterEvent()
    object ResetButtonClicked: CounterEvent()
}
