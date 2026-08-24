package com.my.book.library.feature.splash.intro.viewmodel

import android.app.Application
import app.cash.turbine.test
import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.domain.usecase.data_store.GetMyLibraryInfoUseCase
import com.my.book.library.feature.splash.intro.intent.SplashViewModelEvent
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val app: Application = mockk(relaxed = true)
    private val getMyLibraryInfoUseCase: GetMyLibraryInfoUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        // ViewModel 내부의 viewModelScope가 테스트에서도 즉시 실행되도록 메인 디스패처를 교체한다
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ViewModel을 생성하면 초기 상태는 로딩 중이다`() {
        // Given & When: 아무 인텐트도 보내지 않고 ViewModel만 생성한다
        val viewModel = SplashViewModel(app, getMyLibraryInfoUseCase)

        // Then: splashUiState의 초기값은 isLoading = true 여야 한다
        viewModel.splashUiState.value.isLoading shouldBe true
    }

    @Test
    fun `내 도서관 정보 조회가 끝나면 isLoading이 false가 된다`() = runTest {
        // Given: UseCase가 성공 결과를 즉시 반환하도록 가짜로 설정한다
        coEvery { getMyLibraryInfoUseCase.invoke() } returns
            flowOf(RequestResult.Success(data = mockk<MyRegionAndLibrary>(relaxed = true)))
        val viewModel = SplashViewModel(app, getMyLibraryInfoUseCase)

        // sideEffectEvent도 누군가 받아주지 않으면 Channel.send()가 영원히 멈춘다 (rendezvous channel)
        backgroundScope.launch {
            viewModel.sideEffectEvent.collect {

            }
        }

        viewModel.splashUiState.test {
            awaitItem().isLoading shouldBe true // 초기값

            // When: 조회를 시작한다
            viewModel.intentAction(SplashViewModelEvent.CheckMyLibraryInfo())

            // Then: 조회가 끝나면 isLoading이 false로 바뀐 상태가 흘러나온다
            awaitItem().isLoading shouldBe false
            cancelAndIgnoreRemainingEvents()
        }
    }
}
