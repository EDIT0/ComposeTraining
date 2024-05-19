package com.example.jettriviaapp.repository

import android.util.Log
import com.example.jettriviaapp.data.DataOrException
import com.example.jettriviaapp.model.QuestionItem
import com.example.jettriviaapp.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val questionApi: QuestionApi
) {

    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()

    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = questionApi.getAllQuestions()
            if(dataOrException.data.toString().isNotEmpty()) {
                dataOrException.loading = false
            }
        } catch (e: Exception) {
            dataOrException.e = e
            Log.d("MYTAG", "getAllQuestions: ${dataOrException.e!!.localizedMessage}")
        }
        return dataOrException
    }



}