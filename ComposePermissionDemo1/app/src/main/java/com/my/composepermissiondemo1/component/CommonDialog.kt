package com.my.composepermissiondemo1.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.my.composepermissiondemo1.ui.theme.black
import com.my.composepermissiondemo1.ui.theme.blue500
import com.my.composepermissiondemo1.ui.theme.white

@Composable
fun CommonDialog(
    title: String? = null,
    message: String,
    negativeText: String? = null,
    positiveText: String? = null,
    onNegativeClick: () -> Unit,
    onPositiveClick: () -> Unit,
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean
) {

    Dialog(
        onDismissRequest = {
            onNegativeClick.invoke()
        },
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(white())
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                // Title
                title?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = it,
                            fontSize = 18.sp,
                            color = black(),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }

                // Message
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = message,
                        fontSize = 16.sp,
                        color = black(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier
                    .background(white())
            ) {
                // 부정 버튼
                negativeText?.let {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = blue500(),
                                shape = RectangleShape
                            )
                    ) {

                        Button(
                            onClick = {
                                onNegativeClick.invoke()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = blue500(),
                                    shape = RectangleShape
                                ),
                            colors = ButtonDefaults
                                .buttonColors(
                                    containerColor = blue500(),
                                    contentColor = white()
                                )
                        ) {
                            Text(
                                text = it,
                                fontSize = 15.sp,
                                maxLines = 1
                            )
                        }
                    }
                }

                if (positiveText != null && negativeText != null) {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // 긍정 버튼
                positiveText?.let {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = blue500(),
                                shape = RectangleShape
                            )
                    ) {
                        Button(
                            onClick = {
                                onPositiveClick.invoke()
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = ButtonDefaults
                                .buttonColors(
                                    containerColor = blue500(),
                                    contentColor = white()
                                )
                        ) {
                            Text(
                                text = it,
                                fontSize = 15.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommonDialog() {
    CommonDialog(
        title = "TitleTitleTitleTitleTitleTitleTitleTitleTitleTitle",
        message = "MessageMessage\nMessage\nMessageMessageMessageMessageMessageMessage",
        negativeText = "취소",
        positiveText = "확인",
        onNegativeClick = { },
        onPositiveClick = { },
        dismissOnBackPress = true,
        dismissOnClickOutside = true
    )
}