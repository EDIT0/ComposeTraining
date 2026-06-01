package com.my.book.library.data.repository

import com.my.book.library.core.common.Constant
import com.my.book.library.core.common.util.DataStoreUtil
import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.data.model.MyRegionAndLibraryDto
import com.my.book.library.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    val dataStoreUtil: DataStoreUtil
): DataStoreRepository {

    override suspend fun setMyLibraryInfo(myRegionAndLibrary: MyRegionAndLibrary): Flow<RequestResult<Boolean>> {
        return flow {
            // R.string ID 대신 code(고정값)만 저장
            val dto = MyRegionAndLibraryDto(
                detailRegionCode = myRegionAndLibrary.detailRegion.code,
                library = myRegionAndLibrary.library
            )
            val isSuccess = dataStoreUtil.setJson(DataStoreUtil.MY_LIBRARY_INFO, dto)

            if(isSuccess) {
                emit(RequestResult.Success(true))
            } else {
                emit(RequestResult.Error(Constant.DATA_STORE_SET_FAILURE_CODE, ""))
            }
        }
    }

    override suspend fun getMyLibraryInfo(): Flow<RequestResult<MyRegionAndLibrary>> {
        return flow {
            val dto = dataStoreUtil.getJson(DataStoreUtil.MY_LIBRARY_INFO, MyRegionAndLibraryDto::class.java)

            if(dto != null) {
                // code로 DetailRegion 복원 (R.string ID는 allDetailRegions에서 참조)
                val detailRegion = LibraryData.allDetailRegions.find { it.code == dto.detailRegionCode }
                if(detailRegion != null) {
                    emit(RequestResult.Success(MyRegionAndLibrary(detailRegion, dto.library)))
                } else {
                    emit(RequestResult.Error(Constant.DATA_STORE_GET_FAILURE_CODE, ""))
                }
            } else {
                emit(RequestResult.Error(Constant.DATA_STORE_GET_FAILURE_CODE, ""))
            }
        }
    }
}