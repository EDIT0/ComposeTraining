package com.example.jettip.util


fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if(totalBill > 1 && totalBill.toString().isNotEmpty()) {
        (totalBill * tipPercentage) / 100
    } else {
        0.0
    }
}

fun calculateTotalPerPerson(
    totalBill: Double,
    splitBy: Int,
    tipPercentage: Int
): Double {
    // Tip + 전체 금액
    val bill = (calculateTotalTip(totalBill, tipPercentage) + totalBill)

    return bill / splitBy
}
