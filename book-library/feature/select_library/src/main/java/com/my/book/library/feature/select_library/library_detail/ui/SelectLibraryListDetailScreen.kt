package com.my.book.library.feature.select_library.library_detail.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.my.book.library.core.common.CommonMainViewModel
import com.my.book.library.core.common.component.CommonActionBar
import com.my.book.library.core.common.component.TitleExplanationView1
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.model.res.ResSearchBookLibrary
import com.my.book.library.core.model.res.ResSearchBookLibrary.ResponseData.LibraryWrapper.LibraryInfo
import com.my.book.library.core.resource.Gray500
import com.my.book.library.core.resource.LibraryData
import com.my.book.library.core.resource.R
import com.my.book.library.feature.select_library.library_detail.intent.SelectLibraryListDetailViewModelEvent
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
    val localContext = LocalContext.current

    val commonMainViewModel = commonMainViewModel
    val selectLibraryListDetailViewModel = hiltViewModel<SelectLibraryListDetailViewModel>()


    // DetailRegion, LibraryInfo 저장 - detailRegion, libraryInfo 가 변경될 때만 실행
    LaunchedEffect(detailRegion, libraryInfo) {
        LogUtil.d_dev("LaunchedEffect 실행 - DetailRegion 저장: $detailRegion\nLibraryInfo 저장: ${libraryInfo}")
        selectLibraryListDetailViewModel.intentAction(
            SelectLibraryListDetailViewModelEvent.UpdateDetailRegionAndLibrary(detailRegion = detailRegion, library = libraryInfo)
        )
    }

    val selectLibraryListDetailUiState = selectLibraryListDetailViewModel.selectLibraryListDetailUiState.collectAsStateWithLifecycle()

    LogUtil.d_dev("SelectLibraryListDetailUiState: ${selectLibraryListDetailUiState.value}")

    /**
     * 최초 실행 분기
     */
    var initExecute by rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        if(initExecute) {
            initExecute = false
        }
    }

    LaunchedEffect(key1 = true) {
        selectLibraryListDetailViewModel.sideEffectEvent.collect {
            when(it) {
                is SelectLibraryListDetailViewModel.SideEffectEvent.ShowToast -> {
                    Toast.makeText(localContext, it.message, Toast.LENGTH_SHORT).show()
                }
                is SelectLibraryListDetailViewModel.SideEffectEvent.OnSuccessRegistration -> {
                    onMoveToMain.invoke()
                }
            }
        }
    }

    SelectLibraryListDetailContent(
        localContext = localContext,
        onMoveToMain = onMoveToMain,
        onBackPressed = {
            onBackPressed.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        libraryInfo = libraryInfo,
        selectLibraryListDetailViewModelEvent = {
            selectLibraryListDetailViewModel.intentAction(it)
        }
    )
}

@Composable
fun SelectLibraryListDetailContent(
    localContext: Context,
    onMoveToMain: () -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier,
    libraryInfo: ResSearchBookLibrary.ResponseData.LibraryWrapper,
    selectLibraryListDetailViewModelEvent: (SelectLibraryListDetailViewModelEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CommonActionBar(
                context = localContext,
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                actionBarTitle = libraryInfo.lib.libName?:"",
                isShowBackButton = true,
                onBackClick = {
                    onBackPressed.invoke()
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(state = scrollState)
            ) {
                TitleExplanationView1(
                    modifier = Modifier,
                    title = localContext.getString(R.string.select_library_list_detail_address),
                    explanation = libraryInfo.lib.address?:"-",
                    onExplanationTextClick = {
                        // 구글 지도로 이동 및 좌표 찍기
                    }
                )

                TitleExplanationView1(
                    modifier = Modifier,
                    title = localContext.getString(R.string.select_library_list_detail_connect_number),
                    explanation = libraryInfo.lib.tel?:"-",
                    onExplanationTextClick = {}
                )

                TitleExplanationView1(
                    modifier = Modifier,
                    title = localContext.getString(R.string.select_library_list_detail_fax),
                    explanation = libraryInfo.lib.fax?:"-",
                    onExplanationTextClick = {}
                )

                TitleExplanationView1(
                    modifier = Modifier,
                    title = localContext.getString(R.string.select_library_list_detail_homepage),
                    explanation = libraryInfo.lib.homepage?:"-",
                    onExplanationTextClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                            localContext.startActivity(intent)
                        } catch (e: Exception) {
                            LogUtil.e_dev("URL 열기 실패: ${e.message}")
                        }
                    }
                )

                TitleExplanationView1(
                    modifier = Modifier,
                    title = localContext.getString(R.string.select_library_list_detail_open),
                    explanation = libraryInfo.lib.operatingTime?:"-",
                    onExplanationTextClick = {}
                )

                TitleExplanationView1(
                    modifier = Modifier,
                    title = localContext.getString(R.string.select_library_list_detail_close),
                    explanation = libraryInfo.lib.closed?:"-",
                    onExplanationTextClick = {}
                )
            }

            bottomButtonView(
                title = localContext.getString(R.string.select_library_list_detail_register_button_title),
                onClick = {
                    selectLibraryListDetailViewModelEvent.invoke(SelectLibraryListDetailViewModelEvent.RegisterRegionAndLibrary)
                }
            )

            // 스크롤 위치 확인
            LaunchedEffect(scrollState.value) {
                LogUtil.d_dev("현재 스크롤 위치: ${scrollState.value}")
            }

        }

//        // 하단에 붙을 뷰
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//                .background(
//                    color = Color.White
//                )
//                .padding(
//                    start = 20.dp,
//                    end = 20.dp,
//                    bottom = 20.dp
//                )
//        ) {
//            Button(
//                onClick = {
//
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                Text("시작하기")
//            }
//        }
    }
}

@Composable
fun bottomButtonView(
    title: String,
    textColor: Color = Color.White,
    backgroundColor: Color = Gray500,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
            )
            .noRippleClickable {
                onClick.invoke()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = textColor,
            style = TextStyle(
                fontSize = dpToSp(16.dp)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectLibraryListDetailUiPreview() {
    SelectLibraryListDetailContent(
        localContext = LocalContext.current,
        onMoveToMain = {},
        onBackPressed = {},
        modifier = Modifier,
        libraryInfo = ResSearchBookLibrary.ResponseData.LibraryWrapper(
            lib = LibraryInfo(
                libCode = "libCode",
                libName = "libName",
                address = "address",
                tel = "tel",
                fax = "fax",
                latitude = "latitude",
                longitude = "longitude",
                homepage = "homepage",
                closed = "closed",
                operatingTime = "operatingTime",
                bookCount = "bookCount"
            )
        ),
        selectLibraryListDetailViewModelEvent = { }
    )
}