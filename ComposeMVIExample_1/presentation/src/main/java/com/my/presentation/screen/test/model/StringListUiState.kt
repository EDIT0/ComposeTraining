package com.my.presentation.screen.test.model

data class StringListUiState(
    var stringList: List<String>? = null,
    val isLoading: Boolean = false,
    val code: String? = null,
    val message: String? = null,
    val throwable: Throwable? = null,
    val isDataEmpty: Boolean = false
)