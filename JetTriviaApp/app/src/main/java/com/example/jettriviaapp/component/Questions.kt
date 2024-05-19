package com.example.jettriviaapp.component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettriviaapp.model.QuestionItem
import com.example.jettriviaapp.screens.QuestionsViewModel
import com.example.jettriviaapp.util.AppColors

@Composable
fun Questions(questionsViewModel: QuestionsViewModel) {
    val questions = questionsViewModel.data.value.data?.toMutableList()

    val questionIndex = remember {
        mutableStateOf(0)
    }

    if (questionsViewModel.data.value.loading == true) {
        Log.d("MYTAG", "Loading true")
        CircularProgressIndicator()
    } else {
        Log.d("MYTAG", "Loading false")
        questions?.forEach {
//            Log.d("MYTAG", "Questions: ${it.question}")
        }

        val question = try {
            questions?.get(questionIndex.value)
        } catch (e: Exception) {
            null
        }
        if(questions != null) {
            QuestionDisplay(
                questionItem = question!!,
                questionIndex = questionIndex,
                questionsViewModel
            ) {
                questionIndex.value += 1
            }
        }
    }
}

@Composable
fun QuestionDisplay(
    questionItem: QuestionItem,
    questionIndex: MutableState<Int>,
    questionsViewModel: QuestionsViewModel,
    onNextClicked: (Int) -> Unit = {}
) {
    // 정답 선택지 리스트
    val choicesState = remember(questionItem) {
        questionItem.choices.toMutableList()
    }
    val answerState = remember(questionItem) {
        mutableStateOf<Int?>(null)
    }
    // updateAnswer에서 맞으면 true 틀리면 false를 넣는다.
    val correctAnswerState = remember(questionItem) {
        mutableStateOf<Boolean?>(null)
    }
    val updateAnswer: (Int) -> Unit = remember(questionItem) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == questionItem.answer
        }
    }

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(4.dp),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionTracker(
                counter = questionIndex.value,
                outOf = questionsViewModel.data.value.data!!.size
            )
            DrawDottedLine(pathEffect = pathEffect)

            Column() {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                        .padding(6.dp),
                    textAlign = TextAlign.Start,
                    text = questionItem.question,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = AppColors.mOffWhite,
                )

                choicesState.forEachIndexed { index: Int, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.mOffDarkPurple,
                                        AppColors.mOffDarkPurple
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            colors = RadioButtonDefaults
                                .colors(
                                    selectedColor = if(correctAnswerState.value == true && index == answerState.value) {
                                        Color.Green.copy(alpha = 0.2f)
                                    } else {
                                        Color.Red.copy(alpha = 0.2f)
                                    }
                                ),
                            selected = (answerState.value == index),
                            onClick = {
                                updateAnswer(index)
                            }
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(style = SpanStyle(
                                fontWeight = FontWeight.Light,
                                color = if(correctAnswerState.value == true && index == answerState.value) {
                                    Color.Green
                                } else if(correctAnswerState.value == false && index == answerState.value){
                                    Color.Red
                                } else {
                                    AppColors.mOffWhite
                                },
                                fontSize = 17.sp
                            )) {
                                append(answerText)
                            }
                        }
                        Text(
                            modifier = Modifier
                                .padding(6.dp),
                            text = annotatedString
                        )
                    }
                }

                Button(
                    onClick = {
                        onNextClicked(questionIndex.value)
                    },
                    modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.mLightBlue,
                        contentColor = AppColors.mOffWhite,
                        disabledContainerColor = AppColors.mLightBlue,
                        disabledContentColor = AppColors.mOffWhite,
                    )
                ) {
                    Text(
                        text = "Next",
                        modifier = Modifier
                            .padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionTracker(
    counter: Int = 10,
    outOf: Int = 100
) {
    Text(
        modifier = Modifier
            .padding(20.dp),
        text = buildAnnotatedString {
            // ParagraphStyle: 전체 단락에 적용
            // SpanStyle: 문자 수준에 적용
            withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
                withStyle(style = SpanStyle(color = AppColors.mLightGray, fontWeight = FontWeight.Bold, fontSize = 27.sp)) {
                    val userCounter = counter + 1
                    append("Question ${userCounter}/")
                    withStyle(style = SpanStyle(color = AppColors.mLightGray, fontWeight = FontWeight.Light, fontSize = 14.sp)) {
                        append("${outOf}")
                    }
                }
            }
        }
    )
}

@Composable
fun DrawDottedLine(
    pathEffect: PathEffect
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp),
    ) {
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, y = 0f),
            pathEffect = pathEffect
        )
        
    }
}