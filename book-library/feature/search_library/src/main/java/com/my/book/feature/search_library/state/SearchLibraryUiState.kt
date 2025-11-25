package com.my.book.feature.search_library.state

import com.my.book.library.core.resource.LibraryData

data class SearchLibraryUiState(
    val isFilterOpen: Boolean = false, // 필터 열기/닫기
    val selectionRegion: LibraryData.Region? = null // 선택된 도시
)