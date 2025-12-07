package com.my.book.library.feature.select_library.library_detail.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.my.book.library.core.common.CommonMainViewModel
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.feature.select_library.library_detail.viewmodel.SelectLibraryListDetailViewModel

@Composable
fun SelectLibraryListDetailScreen(
    commonMainViewModel: CommonMainViewModel,
    onMoveToMain: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    detailRegion: LibraryData.DetailRegion,
    libraryInfo: ResSearchBookLibrary.ResponseData.LibraryWrapper
) {

    LogUtil.d_dev("받은 데이터: ${detailRegion}\n${libraryInfo}")
    val context = LocalContext.current

    val commonMainViewModel = commonMainViewModel
    val selectLibraryListDetailViewModel = hiltViewModel<SelectLibraryListDetailViewModel>()

    /**
     * 최초 실행 분기
     */
    var initExecute by rememberSaveable {
        mutableStateOf(true)
    }

}