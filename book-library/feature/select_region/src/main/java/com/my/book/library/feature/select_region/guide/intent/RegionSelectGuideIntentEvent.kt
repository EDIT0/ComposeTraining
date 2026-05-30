package com.my.book.library.feature.select_region.guide.intent

sealed interface RegionSelectGuideViewModelEvent {
    data object CheckRegionAndBack : RegionSelectGuideViewModelEvent
}

sealed interface RegionSelectGuideUiEvent {
}