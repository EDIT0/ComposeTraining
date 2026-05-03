package com.my.book.library.core.common.util

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.my.book.library.core.resource.Transparent

// ================================================================================================
// SystemBarConfig
// ================================================================================================
//
// 각 화면마다 시스템 바(상태바, 내비게이션 바)를 어떻게 표시할지 정의하는 설정값 모음.
//
// 사용처: SystemBarController.Setup() 의 config 파라미터로 전달.
//
// [data class를 사용하는 이유]
//   data class는 equals() / copy() 를 자동 생성한다.
//   Compose 컴파일러는 equals() 가 신뢰 가능한 클래스를 "Stable" 로 간주하여,
//   config 값이 바뀌지 않으면 Setup() 내부 리컴포지션을 건너뛸 수 있다.
//
// ================================================================================================

/**
 * 시스템 바(상태바, 내비게이션 바) 설정 데이터 클래스
 *
 * @param statusBarColor            상태바 배경색 (기본: 투명)
 * @param statusBarDarkIcons        상태바 아이콘 어둡게 표시 여부
 *                                  true  = 어두운 아이콘 → 밝은 배경에 적합
 *                                  false = 밝은 아이콘 → 어두운 배경에 적합
 * @param statusBarVisible          상태바 표시 여부
 * @param useStatusBarSpace         상태바 높이만큼 패딩 공간 사용 여부
 *                                  true  = 콘텐츠가 상태바 아래에서 시작
 *                                  false = 콘텐츠가 상태바 영역까지 확장
 *
 * @param navigationBarColor        내비게이션 바 배경색 (기본: 투명)
 * @param navigationBarDarkIcons    내비게이션 바 아이콘 어둡게 표시 여부
 *                                  true  = 어두운 아이콘 → 밝은 배경에 적합
 *                                  false = 밝은 아이콘 → 어두운 배경에 적합
 * @param navigationBarVisible      내비게이션 바 표시 여부
 * @param useNavigationBarSpace     내비게이션 바 높이만큼 패딩 공간 사용 여부
 *                                  true  = 콘텐츠가 내비게이션 바 위에서 끝남
 *                                  false = 콘텐츠가 내비게이션 바 영역까지 확장
 *
 * @param isImmersiveMode           몰입 모드 (상태바 + 내비게이션 바 모두 숨김)
 *                                  true일 경우 statusBarVisible / navigationBarVisible 무시.
 *                                  화면 가장자리 스와이프로 임시 표시 가능
 * @param enforceSystemBarContrast  시스템 바 자동 대비 강제 적용 여부 (Android 10+ 전용)
 *                                  true  = 아이콘 가독성을 위해 시스템이 배경 대비를 자동 조정
 *                                  false = 설정한 색상 그대로 유지
 */
data class SystemBarConfig(
    // 상태바
    val statusBarColor: Color = Transparent,
    val statusBarDarkIcons: Boolean = true,
    val statusBarVisible: Boolean = true,
    val useStatusBarSpace: Boolean = true,
    // 내비게이션 바
    val navigationBarColor: Color = Transparent,
    val navigationBarDarkIcons: Boolean = true,
    val navigationBarVisible: Boolean = true,
    val useNavigationBarSpace: Boolean = true,
    // 공통
    val isImmersiveMode: Boolean = false,
    val enforceSystemBarContrast: Boolean = false,
)

// ================================================================================================
// SystemBarState
// ================================================================================================
//
// Setup() 이 계산해 낸 시스템 바 관련 수치를 content 람다로 전달하기 위한 홀더 클래스.
// 화면 레이아웃에서 "얼마만큼 패딩을 줘야 하는가"를 알아야 할 때 꺼내 쓴다.
//
// [수명]
//   Setup() 함수 안에서 매 리컴포지션마다 새로 생성된다.
//   WindowInsets(시스템 바 높이) 는 기기 상태에 따라 바뀔 수 있으므로
//   remember 없이 항상 최신 값으로 계산하는 것이 올바르다.
//   화면이 백스택에서 완전히 제거되면 같이 소멸한다.
//
// [@Stable 의 역할]
//   Compose는 리컴포지션 시 파라미터가 바뀌었는지 확인해 불필요한 재렌더링을 줄인다.
//   일반 class는 Compose 컴파일러가 "내부가 언제든 바뀔 수 있다"고 보수적으로 판단해
//   항상 리컴포지션을 유발한다.
//   @Stable 을 붙이면 "public 프로퍼티는 변하지 않으며 equals() 를 신뢰해도 된다"고
//   컴파일러에게 보장하여, 값이 같은 경우 이 객체를 파라미터로 받는 컴포저블을 스킵할 수 있다.
//   (모든 프로퍼티가 val이고 Dp / PaddingValues 모두 Stable 타입이므로 조건 충족)
//
// ================================================================================================

/**
 * 시스템 바 수치 정보를 담는 상태 홀더 클래스.
 * [SystemBarController.Setup] 의 content 람다 파라미터로 전달된다.
 *
 * @property statusBarPadding       상태바 높이에 해당하는 [PaddingValues]
 *                                  [SystemBarConfig.useStatusBarSpace] 가 false 이면 빈 패딩 반환
 * @property navigationBarPadding   내비게이션 바 높이에 해당하는 [PaddingValues]
 *                                  [SystemBarConfig.useNavigationBarSpace] 가 false 이면 빈 패딩 반환
 * @property statusBarHeight        상태바 실제 높이 [Dp] (useStatusBarSpace 값과 무관하게 항상 실제 높이)
 * @property navigationBarHeight    내비게이션 바 실제 높이 [Dp] (useNavigationBarSpace 값과 무관하게 항상 실제 높이)
 */
@Stable
class SystemBarState internal constructor(
    val statusBarPadding: PaddingValues,
    val navigationBarPadding: PaddingValues,
    val statusBarHeight: Dp,
    val navigationBarHeight: Dp,
)

// ================================================================================================
// SystemBarController
// ================================================================================================
//
// 시스템 바의 색상·아이콘·표시 여부를 화면 단위로 제어하는 컨트롤러.
// Setup() 하나로 "설정 적용 + 배경 드로잉 + 수치 제공" 을 모두 처리한다.
//
// [배경색을 window API 대신 Compose Box 로 그리는 이유]
//   Android 15(API 35) 부터 enableEdgeToEdge() 가 강제 적용되면서
//   window.statusBarColor / window.navigationBarColor 는 deprecated 되어 무시된다.
//   따라서 배경색은 상태바/내비게이션 바 높이만큼의 Box 를 화면 최상단·최하단에
//   직접 그리는 방식으로 구현한다.
//
// [object 로 선언하는 이유]
//   인스턴스를 생성할 필요 없이 SystemBarController.Setup(...) 으로 바로 호출하기 위해
//   싱글턴 object 로 선언한다. 내부 상태를 보관하지 않으므로 object 가 적합하다.
//
// ================================================================================================

/**
 * 시스템 바(상태바, 내비게이션 바)를 제어하는 컨트롤러.
 *
 * 각 화면의 최상단에서 [Setup] 을 호출하여 해당 화면이 보이는 동안 시스템 바 설정을 유지한다.
 * 다른 화면으로 이동하면 그 화면의 [Setup] 설정이 덮어씌워진다.
 *
 * **사용 예시:**
 * ```kotlin
 * @Composable
 * fun MyScreen() {
 *     SystemBarController.Setup(
 *         config = SystemBarConfig(
 *             statusBarColor = Black,
 *             statusBarDarkIcons = false,
 *             useStatusBarSpace = true,
 *             navigationBarColor = White,
 *             navigationBarDarkIcons = true,
 *             useNavigationBarSpace = false
 *         )
 *     ) { state ->
 *         Box(modifier = Modifier.padding(top = state.statusBarHeight)) {
 *             // 화면 콘텐츠
 *         }
 *     }
 * }
 * ```
 */
object SystemBarController {

    /**
     * [SystemBarConfig] 에 따라 시스템 바를 설정하고 [content] 를 표시한다.
     *
     * @param config    적용할 시스템 바 설정. 기본값은 투명 배경, 어두운 아이콘, 모두 표시
     * @param modifier  최외곽 [Box] 에 적용할 [Modifier]. 기본값은 fillMaxSize
     * @param content   [SystemBarState] 를 인자로 받는 화면 콘텐츠 람다
     */
    @Composable
    fun Setup(
        config: SystemBarConfig = SystemBarConfig(),
        modifier: Modifier = Modifier.fillMaxSize(),
        content: @Composable BoxScope.(state: SystemBarState) -> Unit
    ) {
        // ────────────────────────────────────────────────────────────────────
        // 1. 시스템 바 높이 계산
        //
        // WindowInsets: 시스템이 UI 영역을 어디까지 차지하는지 알려주는 값.
        //   - statusBars    → 상단 상태바 높이
        //   - navigationBars → 하단 내비게이션 바 높이 (제스처 / 버튼 모두 포함)
        //
        // LocalDensity: px → dp 단위 변환에 필요한 화면 밀도(density) 정보.
        //   getTop() / getBottom() 은 px 단위를 반환하므로 .toDp() 로 변환한다.
        // ────────────────────────────────────────────────────────────────────
        val view = LocalView.current
        val density = LocalDensity.current

        val statusBarInsets = WindowInsets.statusBars
        val navigationBarInsets = WindowInsets.navigationBars

        val statusBarHeight: Dp = with(density) { statusBarInsets.getTop(this).toDp() }
        val navigationBarHeight: Dp = with(density) { navigationBarInsets.getBottom(this).toDp() }

        // ────────────────────────────────────────────────────────────────────
        // 2. useSpace 여부에 따라 패딩값 결정
        //
        // useStatusBarSpace = true  → statusBarHeight 만큼 상단 패딩 제공
        // useStatusBarSpace = false → 빈 PaddingValues 제공 (콘텐츠가 상태바 뒤까지 확장)
        // navigationBarSpace 도 동일한 방식으로 처리
        // ────────────────────────────────────────────────────────────────────
        val statusBarPadding = if (config.useStatusBarSpace) {
            statusBarInsets.asPaddingValues()
        } else {
            PaddingValues()
        }
        val navigationBarPadding = if (config.useNavigationBarSpace) {
            navigationBarInsets.asPaddingValues()
        } else {
            PaddingValues()
        }

        // ────────────────────────────────────────────────────────────────────
        // 3. Window 설정 적용 (아이콘 색상 / 표시 여부)
        //
        // [isInEditMode 체크 이유]
        //   Android Studio Preview 환경에서는 실제 Activity Window 가 존재하지 않아
        //   context as ComponentActivity 캐스팅이 실패한다. isInEditMode 가 true 이면
        //   Preview 환경이므로 Window 관련 코드를 건너뛴다.
        //
        // [SideEffect 를 사용하는 이유]
        //   Compose 리컴포지션 중에는 side effect(UI 외부에 영향을 주는 동작)를 직접 실행하면
        //   여러 번 호출될 수 있어 위험하다. SideEffect 는 리컴포지션이 성공적으로 완료된
        //   직후에 딱 한 번 실행되는 것을 보장하므로 Window 설정 변경에 적합하다.
        //
        // [WindowInsetsControllerCompat 역할]
        //   Android 버전별 차이를 추상화하여 상태바/내비게이션 바의 아이콘 색상과
        //   표시 여부를 일관된 API 로 제어할 수 있게 해 준다.
        // ────────────────────────────────────────────────────────────────────
        if (!view.isInEditMode) {
            val window = (view.context as ComponentActivity).window
            val insetsController = WindowInsetsControllerCompat(window, view)

            SideEffect {
                // 아이콘 색상
                // isAppearanceLightStatusBars = true  → 어두운(Dark) 아이콘 표시
                // isAppearanceLightStatusBars = false → 밝은(Light) 아이콘 표시
                insetsController.isAppearanceLightStatusBars = config.statusBarDarkIcons
                insetsController.isAppearanceLightNavigationBars = config.navigationBarDarkIcons

                // 자동 대비 강제 (Android 10, API 29+)
                // true  → 배경색에 관계없이 시스템이 아이콘 가독성을 위해 반투명 스크림을 추가
                // false → 개발자가 설정한 배경색 그대로 유지
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.isStatusBarContrastEnforced = config.enforceSystemBarContrast
                    window.isNavigationBarContrastEnforced = config.enforceSystemBarContrast
                }

                // 표시/숨김 처리
                if (config.isImmersiveMode) {
                    // 몰입 모드: 상태바 + 내비게이션 바 동시에 숨김
                    // BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE → 가장자리 스와이프로 임시 표시 가능
                    insetsController.hide(WindowInsetsCompat.Type.systemBars())
                    insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                } else {
                    // 몰입 모드 해제 시 BEHAVIOR_DEFAULT 로 복원
                    insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                    // 상태바 개별 표시/숨김
                    if (config.statusBarVisible) {
                        insetsController.show(WindowInsetsCompat.Type.statusBars())
                    } else {
                        insetsController.hide(WindowInsetsCompat.Type.statusBars())
                    }
                    // 내비게이션 바 개별 표시/숨김
                    if (config.navigationBarVisible) {
                        insetsController.show(WindowInsetsCompat.Type.navigationBars())
                    } else {
                        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
                    }
                }
            }
        }

        // ────────────────────────────────────────────────────────────────────
        // 4. 수치 정보를 SystemBarState 로 묶어 content 람다에 전달
        // ────────────────────────────────────────────────────────────────────
        val state = SystemBarState(
            statusBarPadding = statusBarPadding,
            navigationBarPadding = navigationBarPadding,
            statusBarHeight = statusBarHeight,
            navigationBarHeight = navigationBarHeight,
        )

        // ────────────────────────────────────────────────────────────────────
        // 5. 배경색 드로잉 + content 렌더링
        //
        // [배경색을 Box 로 직접 그리는 이유]
        //   Android 15(API 35)+ 에서 enableEdgeToEdge() 가 강제 적용되면서
        //   window.statusBarColor / window.navigationBarColor 는 무시된다.
        //   대신 상태바·내비게이션 바 높이만큼의 Box 를 화면 최상단·최하단에 직접 그려
        //   배경색을 표현한다.
        //
        // [레이어 순서]
        //   Box 는 먼저 선언한 자식이 아래, 나중에 선언한 자식이 위에 그려진다.
        //   1) 상태바 배경 Box     ← 최하단 레이어
        //   2) 내비게이션 바 배경 Box
        //   3) content (실제 화면) ← 최상단 레이어
        //
        // [Transparent 일 때 Box 를 그리지 않는 이유]
        //   배경이 투명이면 굳이 빈 Box 를 추가할 필요가 없어 불필요한 컴포저블 생성을 방지한다.
        // ────────────────────────────────────────────────────────────────────
        Box(modifier = modifier) {
            // 상태바 배경 Box (투명이 아닐 때만 그림)
            if (config.statusBarColor != Transparent) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(statusBarHeight)        // 상태바 높이만큼
                        .background(config.statusBarColor)
                        .align(Alignment.TopCenter)     // 화면 최상단에 고정
                )
            }
            // 내비게이션 바 배경 Box (투명이 아닐 때만 그림)
            if (config.navigationBarColor != Transparent) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(navigationBarHeight)    // 내비게이션 바 높이만큼
                        .background(config.navigationBarColor)
                        .align(Alignment.BottomCenter)  // 화면 최하단에 고정
                )
            }
            // 실제 화면 콘텐츠 (배경 Box 위에 렌더링)
            content(state)
        }
    }
}
