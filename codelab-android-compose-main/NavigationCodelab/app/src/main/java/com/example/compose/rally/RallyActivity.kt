/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.rally

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.compose.rally.ui.accounts.AccountsScreen
import com.example.compose.rally.ui.accounts.SingleAccountScreen
import com.example.compose.rally.ui.bills.BillsScreen
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.overview.OverviewScreen
import com.example.compose.rally.ui.theme.RallyTheme

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
//        var currentScreen: RallyDestination by remember { mutableStateOf(Overview) }

        // NavController 생성
        val navController = rememberNavController()

        // 현재 네비게이션 백스택 상태를 Compose의 상태로 관찰
        // navController.navigate() 로 화면이 바뀔 때마다 자동으로 recomposition 트리거됨
        val currentBackStack by navController.currentBackStackEntryAsState()

        // 현재 백스택의 최상단(destination)을 가져옴
        // 즉, 지금 표시 중인 화면의 route 정보를 나타냄
        val currentDestination = currentBackStack?.destination

        // 현재 화면(route)에 해당하는 RallyDestination 객체를 찾음
        // rallyTabRowScreens 리스트 안에서 현재 destination의 route와 일치하는 항목을 반환
        // 만약 일치하는 게 없으면 기본값으로 Accounts 화면을 사용
        val currentScreen = rallyTabRowScreens.find { it.route == currentDestination?.route } ?: Accounts

        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = rallyTabRowScreens,
                    onTabSelected = { screen ->
//                        currentScreen = screen
//                        navController.navigate(screen.route)
                        navController
                            .navigateSingleTopTo(screen.route)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            Log.d("MYTAG", "${currentScreen.route}")
//            Box(Modifier.padding(innerPadding)) {
//                currentScreen.screen()
//            }
            RallyNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Overview.route,
        modifier = modifier
    ) {
        composable(
            route = Overview.route,
            content = {
//                    Overview.screen()
                OverviewScreen(
                    onClickSeeAllAccounts = {
                        navController.navigateSingleTopTo(Accounts.route)
                    },
                    onClickSeeAllBills = {
                        navController.navigateSingleTopTo(Bills.route)
                    },
                    onAccountClick = { accountType ->
                        Log.d("MYTAG", "onAccountClick: ${accountType}")
                        navController
                            .navigateSingleTopTo("${SingleAccount.route}/$accountType")
                    }
                )
            }
        )

        composable(
            route = Accounts.route,
            content = {
//                    Accounts.screen()
                AccountsScreen(
                    onAccountClick = { accountType ->
                        Log.d("MYTAG", "onAccountClick: ${accountType}")
                        navController
                            .navigateSingleTopTo("${SingleAccount.route}/$accountType")
                    }
                )
            }
        )

        composable(
            route = Bills.route,
            content = {
//                    Bills.screen()
                BillsScreen()
            }
        )

//        composable(
//            route = "${SingleAccount.route}/{${SingleAccount.accountTypeArg}}",
//            arguments = listOf(
//                navArgument(SingleAccount.accountTypeArg) { type = NavType.StringType }
//            ),
//            content = { navBackStackEntry ->
//                // Retrieve the passed argument
//                val accountType = navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)
//
//                // Pass accountType to SingleAccountScreen
//                SingleAccountScreen(accountType = accountType)
//            }
//        )

        /**
         * 테스트 방법
         * adb shell am start -d "rally://single_account/Checking" -a android.intent.action.VIEW
         *
         * # 기본 구조
         * adb shell am start [옵션]
         *
         * # 주요 옵션들
         * -a <ACTION>     # Intent 액션 (예: android.intent.action.VIEW)
         * -d <DATA_URI>   # Data URI (딥링크)
         * -n <COMPONENT>  # 특정 컴포넌트 실행
         * -e <EXTRA_KEY> <EXTRA_VALUE>  # Extra 데이터 전달
         * -f <FLAG>       # Intent 플래그
         */
        composable(
            route = SingleAccount.routeWithArgs,
            arguments = listOf(
                navArgument(SingleAccount.accountTypeArg) { type = NavType.StringType }
            ),
            deepLinks = SingleAccount.deepLinks,
            content = { navBackStackEntry ->
                // Retrieve the passed argument
                val accountType = navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)

                // Pass accountType to SingleAccountScreen
                SingleAccountScreen(accountType = accountType)
            }
        )
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

private fun NavHostController.navigateToSingleAccount(accountType: String) {
    this.navigateSingleTopTo("${SingleAccount.route}/$accountType")
}