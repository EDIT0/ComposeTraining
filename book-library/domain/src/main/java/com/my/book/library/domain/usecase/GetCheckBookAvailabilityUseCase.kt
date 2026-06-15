package com.my.book.library.domain.usecase

import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.model.req.ReqCheckBookAvailability
import com.my.book.library.core.model.res.ResCheckBookAvailability
import com.my.book.library.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCheckBookAvailabilityUseCase @Inject constructor(
    val repository: Repository
) {

    suspend fun invoke(
        reqCheckBookAvailability: ReqCheckBookAvailability
    ): Flow<RequestResult<ResCheckBookAvailability>> {
        return repository.getCheckBookAvailability(reqCheckBookAvailability = reqCheckBookAvailability)
    }

}
