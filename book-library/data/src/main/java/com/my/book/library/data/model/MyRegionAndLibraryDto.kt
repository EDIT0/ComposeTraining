package com.my.book.library.data.model

import com.my.book.library.core.model.res.ResSearchBookLibrary

/**
 * DataStore 저장 전용 DTO.
 *
 * DetailRegion은 @StringRes 정수 ID(regionNameRes, districtNameRes)를 포함하므로
 * 빌드마다 ID가 재배정될 수 있어 직렬화하면 안 됩니다.
 * 대신 빌드와 무관하게 고정된 detailRegionCode만 저장하고,
 * 불러올 때 LibraryData.allDetailRegions에서 코드로 복원합니다.
 */
data class MyRegionAndLibraryDto(
    val detailRegionCode: Int,
    val library: ResSearchBookLibrary.ResponseData.LibraryWrapper?
)
