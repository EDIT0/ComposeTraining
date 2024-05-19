package com.example.jettriviaapp.screens

import androidx.compose.runtime.Composable
import com.example.jettriviaapp.component.Questions

@Composable
fun TriviaHome(questionsViewModel: QuestionsViewModel) {
    Questions(questionsViewModel)
}