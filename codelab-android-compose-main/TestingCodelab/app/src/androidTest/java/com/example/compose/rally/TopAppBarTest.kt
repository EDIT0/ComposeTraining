package com.example.compose.rally

import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.compose.rally.ui.components.RallyTopAppBar
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

    /**
     * Compose UI 테스트를 위한 기본 규칙(Rule)
     * - Compose UI를 테스트 환경에서 실행하고 조작할 수 있게 함
     */
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myTest() {
        composeTestRule.setContent {
            Text("Yon can set any Compose content!")
        }
    }

    @Test
    fun rallyTopAppBarTest() {
        // RallyScreen: 앱의 화면 탭(Accounts, Overview, Bills 등)을 나타내는 enum 클래스
        val allScreens = RallyScreen.values().toList()

        /**
         * setContent()
         * - 실제 테스트 환경에서 Composable UI를 렌더링함
         * - 이 블록 내부에 Compose UI를 직접 배치할 수 있음
         */
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = {

                },
                currentScreen = RallyScreen.Accounts
            )
        }

        /**
         * UI 테스트 핵심 메서드 설명
         *
         * onNodeWithText("문자열")
         *   - 화면에서 특정 텍스트를 가진 노드를 찾음
         *   - 예: composeTestRule.onNodeWithText("Accounts")
         *
         * onNodeWithContentDescription("문자열")
         *   - contentDescription 속성값으로 노드를 찾음
         *   - 주로 접근성(Accessibility) 관련 속성을 이용해 찾을 때 사용
         *   - 예: 아이콘, 버튼 등 시각적으로는 텍스트가 없지만 contentDescription이 설정된 경우
         *
         * isSelected()
         *   - 노드의 '선택됨(selected)' 상태를 나타내는 Matcher
         *   - 예: 현재 탭이 선택된 상태인지 확인할 때 사용
         *
         * hasContentDescription("문자열")
         *   - 해당 contentDescription을 가진 노드인지 판별하는 Matcher
         *   - 예: assert(hasContentDescription("Accounts"))
         *
         * assertIsSelected()
         *   - 선택 상태임을 검증하는 Assertion 함수
         *   - 선택되지 않았을 경우 테스트 실패
         */

        // RallyTopAppBar의 Accounts 탭(contentDescription = "Accounts")을 찾아 선택 상태인지 검증
        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()
    }

    @Test
    fun rallyTopAppBarTest_currentLabelExists() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }

        composeTestRule.onRoot().printToLog("currentLabelExists") // UI 트리 구조를 디버깅

        composeTestRule
            .onNodeWithText(RallyScreen.Accounts.name)
            .assertExists() // 위에서 탐색한 노드가 존재하지 않으면 테스트 실패
    }

    @Test
    fun `RallyTopAppBar의_다른_탭을_클릭하면_선택_항목이_변경되는지_확인`() {
        val allScreens = RallyScreen.values().toList()

        composeTestRule.setContent {
            var currentScreen by remember {
                mutableStateOf(RallyScreen.Accounts)
            }

            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = {
                    currentScreen = it
                },
                currentScreen = currentScreen
            )
        }

//        allScreens.forEach { screen ->
//            composeTestRule
//                .onNodeWithContentDescription(screen.name)
//                .performClick()
//
//            composeTestRule
//                .onNodeWithContentDescription(screen.name)
//                .assertIsSelected()
//        }

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Overview.name)
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Overview.name)
            .assertIsSelected()

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Bills.name)
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Bills.name)
            .assertIsSelected()
    }
}