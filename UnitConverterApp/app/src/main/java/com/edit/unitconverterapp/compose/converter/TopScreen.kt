package com.edit.unitconverterapp.compose.converter

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.edit.unitconverterapp.data.Conversion
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun TopScreen(
    list: List<Conversion>,
    save: (String, String) -> Unit
) {

    Log.i("MYTAG", "TopScreen")

    val selectedConversion: MutableState<Conversion?> = remember {
        mutableStateOf(null)
    }

    val inputText: MutableState<String> = remember {
        mutableStateOf("")
    }

    val typedValue = remember {
        mutableStateOf("0.0")
    }

    ConversionMenu(list = list) {
        selectedConversion.value = it
        typedValue.value = "0.0"
    }

    selectedConversion.value?.let {
        InputBlock(conversion = it, inputText = inputText) { input ->
            typedValue.value = input
            Log.i("MYTAG", "User typed ${input}")
        }
    }

    if(typedValue.value != "0.0") {
        Log.i("MYTAG", "이거 실행")
        val input = typedValue.value.toDouble()
        val multiply = selectedConversion.value!!.multiplyBy
        val result = input * multiply


        val df = DecimalFormat("#.####")
        df.roundingMode = RoundingMode.DOWN
        val roundedResult = df.format(result)

        val message1 = "${typedValue.value} ${selectedConversion.value!!.convertFrom} is equal to"
        val message2 = "${roundedResult} ${selectedConversion.value!!.convertTo}"

        save(message1, message2)
        ResultBlock(message1 = message1, message2 = message2)
    }
}