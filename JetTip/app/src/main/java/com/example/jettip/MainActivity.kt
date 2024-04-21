package com.example.jettip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettip.components.InputField
import com.example.jettip.ui.theme.JetTipTheme
import com.example.jettip.util.calculateTotalPerPerson
import com.example.jettip.util.calculateTotalTip
import com.example.jettip.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {

                TotalUI()

            }
        }
    }
}

@Preview
@Composable
fun TotalUI() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
//            TopHeader()
            MainContent()
        }
    }
}

@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 134.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(150.dp)
//            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp)))
            .shadow(10.dp, shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = colorResource(id = R.color.purple_200)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview
@Composable
fun MainContent() {

    // 나눌 값
    val splitByState = remember {
        mutableStateOf(1)
    }

    val range = IntRange(start = 1, endInclusive = 100)

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }

    BillForm(
        Modifier,
        range,
        splitByState,
        tipAmountState,
        totalPerPersonState,
    ) { onValChange ->
        Log.d("MYTAG", "MainContent: ${onValChange}")
    }

}

@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    splitByState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    onValChange: (String) -> Unit = {}
) {
    // 현재 입력 값
    val totalBillState = remember {
        mutableStateOf("")
    }

    // 입력 값이 Empty인지 확인
    val validState: Boolean = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    // 키보드 컨트롤
    val keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current

    // 슬라이더 값
    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = remember {
        mutableStateOf(0.0)
    }

    TopHeader(totalPerPersonState.value)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 2.dp, color = colorResource(id = R.color.FFD5D5D5))
    ) {
        Column(
            modifier = modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    // Enter 눌렀을 경우
                    if (!validState) {
                        Log.d("MYTAG", "값 없음")
                        return@KeyboardActions
                    }

                    onValChange(totalBillState.value.trim())

                    keyboardController?.hide()
                }
            )
            if (validState) {
                // Split
                Row(
                    modifier = modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Split",
                        modifier = modifier
                            .align(alignment = Alignment.CenterVertically)
                    )
//                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                Log.d("MYTAG", "Minus Clicked")
                                splitByState.value = if(splitByState.value > 1) {
                                    splitByState.value - 1
                                } else {
                                    1
                                }
                                totalPerPersonState.value = calculateTotalPerPerson(totalBillState.value.toDouble(), splitByState.value, tipPercentage.value.toInt())
                            }
                        )

                        Text(
                            modifier = modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 9.dp),
                            text = splitByState.value.toString()
                        )

                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                Log.d("MYTAG", "Plus Clicked")
                                if(splitByState.value < range.last) {
                                    splitByState.value += 1
                                }
                                totalPerPersonState.value = calculateTotalPerPerson(totalBillState.value.toDouble(), splitByState.value, tipPercentage.value.toInt())
                            }
                        )
                    }
                }

                // Tip Row
                Row(
                    modifier = modifier
                        .padding(10.dp)
                ) {
                    Text(
                        modifier = modifier
                            .align(alignment = Alignment.CenterVertically),
                        text = "Tip"
                    )
//                    Spacer(modifier = Modifier.foldIn().width(200.dp))
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp),
                        textAlign = TextAlign.End,
                        text = "${tipAmountState.value}"
                    )
                }

                // Percentage
                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${tipPercentage.value} %"
                    )

                    Spacer(modifier = modifier.height(14.dp))

                    // Slider
                    Slider(
                        value = sliderPositionState.value,
                        onValueChange = {
                            sliderPositionState.value = it
                            tipPercentage.value = "%.0f".format(sliderPositionState.value * 100).toDouble()
                            tipAmountState.value = calculateTotalTip(totalBillState.value.toDouble(), tipPercentage.value.toInt())
                            totalPerPersonState.value = calculateTotalPerPerson(totalBillState.value.toDouble(), splitByState.value, tipPercentage.value.toInt())
                            Log.d("MYTAG", "${it * 100}, ${tipPercentage}")
                        },
                        modifier = modifier.padding(horizontal = 16.dp),
//                        steps = 5,
                        onValueChangeFinished = {
//                            tipAmountState.value = calculateTotalTip(totalBillState.value.toDouble(), tipPercentage.toInt())
//                            totalPerPersonState.value = calculateTotalPerPerson(totalBillState.value.toDouble(), splitByState.value, tipPercentage.toInt())
                            Log.d("MYTAG", "onValueChangeFinished()")
                        }
                    )
                }
            } else {
                Box {

                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}