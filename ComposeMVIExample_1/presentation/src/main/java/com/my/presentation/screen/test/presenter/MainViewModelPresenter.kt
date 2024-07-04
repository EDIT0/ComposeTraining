package com.my.presentation.screen.test.presenter

sealed class MainViewModelPresenter {
    class GetStringList(val data1: String) : MainViewModelPresenter()
}