package com.my.book.library.feature.splash.intro.viewmodel

import android.app.Application
import app.cash.turbine.test
import com.my.book.library.core.model.local.MyRegionAndLibrary
import com.my.book.library.core.model.network.RequestResult
import com.my.book.library.domain.usecase.data_store.GetMyLibraryInfoUseCase
import com.my.book.library.feature.splash.intro.intent.SplashViewModelEvent
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
        // 일반 함수: every { }
        // Suspend 함수: coEvery { }
        coEvery { getMyLibraryInfoUseCase.invoke() } returns
                flowOf(RequestResult.Success(data = mockk<MyRegionAndLibrary>(relaxed = true)))
        val viewModel = SplashViewModel(app, getMyLibraryInfoUseCase)

        // sideEffectEvent도 누군가 받아주지 않으면 Channel.send()가 영원히 멈춘다 (rendezvous channel)
        // _sideEffectEvent.send(SideEffectEvent.OnMoveToMain()) 해당 이벤트 받기 위해 추가
        backgroundScope.launch {
            viewModel.sideEffectEvent.collect {}
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

    @Test
    fun `내 도서관 정보 유무에 대해 Success가 아닌 모든 결과는 지역 선택 화면으로 이동하고 isLoading이 false가 된다`() = runTest {
        val failureCases = listOf(
            RequestResult.Error<MyRegionAndLibrary>(code = 1, message = ""),
            RequestResult.AuthError<MyRegionAndLibrary>(code = 1, message = ""),
            RequestResult.ConnectionError<MyRegionAndLibrary>(),
            RequestResult.DataEmpty<MyRegionAndLibrary>()
        )

        failureCases.forEach { result ->
            // Given: UseCase가 실패 결과를 반환하도록 설정한다
            coEvery { getMyLibraryInfoUseCase.invoke() } returns flowOf(result)
            val viewModel = SplashViewModel(app = app, getMyLibraryInfoUseCase = getMyLibraryInfoUseCase)

            // sideEffectEvent로 실제 어떤 이벤트가 왔는지 기록해둔다 (Success 테스트처럼 그냥 흘려버리기만 하면 검증이 안 됨)
            var receivedSideEffect: SplashViewModel.SideEffectEvent? = null
            backgroundScope.launch {
                viewModel.sideEffectEvent.collect { receivedSideEffect = it }
            }

            viewModel.splashUiState.test {
                awaitItem().isLoading shouldBe true // 초기값

                // When: 조회를 시작한다
                viewModel.intentAction(SplashViewModelEvent.CheckMyLibraryInfo())

                // Then: 실패했어도 로딩은 끝나야 한다
                awaitItem().isLoading shouldBe false
                cancelAndIgnoreRemainingEvents()
            }

            // Then: 지역 선택 화면으로 이동하는 사이드이펙트가 발생해야 한다
            receivedSideEffect.shouldBeInstanceOf<SplashViewModel.SideEffectEvent.OnMoveToSelectLibrary>()
        }
    }

    @Test
    fun `내 도서관 정보 조회 중 예외가 발생하면 토스트 이벤트가 발생하고 isLoading이 false가 된다`() = runTest {
        // Given: UseCase가 Flow 도중 예외를 던지도록 설정한다
        coEvery { getMyLibraryInfoUseCase.invoke() } returns flow { throw RuntimeException("network error") }
        val viewModel = SplashViewModel(app = app, getMyLibraryInfoUseCase = getMyLibraryInfoUseCase)

        // sideEffectEvent로 실제 어떤 이벤트가 왔는지 기록해둔다
        var receivedSideEffect: SplashViewModel.SideEffectEvent? = null
        backgroundScope.launch {
            viewModel.sideEffectEvent.collect { receivedSideEffect = it }
        }

        viewModel.splashUiState.test {
            awaitItem().isLoading shouldBe true // 초기값

            // When: 조회를 시작한다
            viewModel.intentAction(SplashViewModelEvent.CheckMyLibraryInfo())

            // Then: 예외가 나도 로딩은 끝나야 한다
            awaitItem().isLoading shouldBe false
            cancelAndIgnoreRemainingEvents()
        }

        // Then: 에러 메시지를 담은 토스트 사이드이펙트가 발생해야 한다
        receivedSideEffect.shouldBeInstanceOf<SplashViewModel.SideEffectEvent.ShowToast>()
        (receivedSideEffect as SplashViewModel.SideEffectEvent.ShowToast).message shouldBe "network error"
    }
}
