package com.my.domain.usecase.test

import com.my.domain.model.base.RequestResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStringListUseCase @Inject constructor(
    // Repository
) {

    suspend fun invoke(data1: String, key: String): Flow<RequestResult<List<String>>> {
        return flow<RequestResult<List<String>>> {
            delay(3000L)
            if(key == "Success") {
                emit(RequestResult.Success(listOf(data1, "A", "B", "C")))
            } else if(key == "Error"){
                emit(RequestResult.Error("Error Code", "Error Message"))
            }

//            emit(RequestResult.DataEmpty())

//            emit(RequestResult.ConnectionError("ConnectionError Code", "ConnectionError Message"))
        }
    }

}