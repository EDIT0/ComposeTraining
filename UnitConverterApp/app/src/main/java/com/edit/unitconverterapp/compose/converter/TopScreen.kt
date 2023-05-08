package com.edit.unitconverterapp.compose.converter

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.edit.unitconverterapp.data.Conversion
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun TopScreen(
    list: List<Conversion>,
    selectedConversion: MutableState<Conversion?>,
    inputText: MutableState<String>,
    typedValue: MutableState<String>,
    isLandscape: Boolean,
    save: (String, String) -> Unit
) {

    Log.i("MYTAG", "TopScreen")

    var toSave by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        ConversionMenu(list = list, isLandscape) {
            selectedConversion.value = it
            typedValue.value = "0.0"
        }

        selectedConversion.value?.let {
            InputBlock(conversion = it, inputText = inputText, isLandscape) { input ->
                typedValue.value = input
                toSave = true
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

            if(toSave) {
                save(message1, message2)
                toSave = false
            }
            ResultBlock(message1 = message1, message2 = message2)
        }
    }
}