package com.example.compose.rally

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var navController: TestNavHostController

    @Before
    fun setupRallyNavHost() {
        // Compose 테스트 환경에 테스트할 Composable 설정
        composeTestRule.setContent {
            // 테스트용 NavController 생성 (실제 앱의 NavController를 흉내냄)
            // LocalContext.current로 현재 테스트 환경의 Context 가져옴
            navController = TestNavHostController(context = LocalContext.current)

            // NavController에 ComposeNavigator 추가
            // ComposeNavigator는 Compose 화면 간 이동을 처리하는 Navigator
            // 이게 없으면 Navigation이 작동하지 않음
            navController.navigatorProvider.addNavigator(
                navigator = ComposeNavigator()
            )

            // 테스트하려는 NavHost를 렌더링
            // 생성한 navController를 전달하여 네비게이션 동작 테스트 가능
            RallyNavHost(navController = navController)
        }
    }

    @Test
    fun rallyNavHost_verifyOverviewStartDestination() {
        // Compose UI 테스트 시작
        composeTestRule
            // "Overview Screen 임"이라는 contentDescription을 가진 UI 요소 찾기
            // contentDescription은 접근성을 위해 설정한 설명 텍스트
            .onNodeWithContentDescription("Overview Screen 임")
            // 해당 UI 요소가 화면에 표시되는지 확인 (assert)
            // 표시되지 않으면 테스트 실패
            .assertIsDisplayed()
    }

    @Test
    fun rallyNavHost_clickAllAccount_navigatesToAccounts() {
        // "All Accounts"라는 contentDescription을 가진 UI 요소 찾기
        // (일반적으로 버튼, 카드, 또는 클릭 가능한 아이템)
        composeTestRule
            .onNodeWithContentDescription("All Accounts")
            // 해당 UI 요소를 클릭 (사용자가 탭하는 동작 시뮬레이션)
            .performClick()

        // 클릭 후 화면 전환이 일어났는지 검증
        composeTestRule
            // "Accounts Screen"이라는 contentDescription을 가진 UI 요소 찾기
            // (Accounts 화면의 루트 요소나 주요 식별 요소)
            .onNodeWithContentDescription("Accounts Screen")
            // 해당 화면이 실제로 표시되는지 확인 (assert)
            // 표시되지 않으면 테스트 실패 → 네비게이션이 제대로 작동하지 않은 것
            .assertIsDisplayed()
    }

    @Test
    fun rallyNavHost_clickAllBills_navigateToBills() {
        // "All Bills"라는 contentDescription(또는 텍스트)을 가진 UI 요소 찾기
        composeTestRule.onNodeWithContentDescription("All Bills")
            // 화면에 보이지 않는 경우 스크롤하여 해당 요소를 화면에 표시
            // (Overview 화면이 길어서 "All Bills"가 화면 밖에 있을 수 있음)
            .performScrollTo()
            // 해당 UI 요소 클릭 (사용자가 탭하는 동작 시뮬레이션)
            .performClick()

        // NavController의 현재 백스택 엔트리에서 목적지(destination)의 route 가져오기
        // currentBackStackEntry = 현재 화면의 백스택 정보
        // destination = 현재 화면의 목적지 정보
        // route = 해당 목적지의 경로 문자열 (예: "bills", "accounts" 등)
        val route = navController.currentBackStackEntry?.destination?.route
        // 현재 route가 "bills"인지 확인 (assert)
        // Bills 화면으로 제대로 이동했는지 검증
        // 실패 시: 네비게이션이 잘못되었거나 route 이름이 다름
        assertEquals(route, "bills")
    }

}