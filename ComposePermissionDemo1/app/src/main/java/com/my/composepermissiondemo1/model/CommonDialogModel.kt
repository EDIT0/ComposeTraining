package com.my.composepermissiondemo1.model

data class CommonDialogModel(
    var isShow: Boolean = false,
    val message: String = "",
    val buttonText: String = "확인"
)