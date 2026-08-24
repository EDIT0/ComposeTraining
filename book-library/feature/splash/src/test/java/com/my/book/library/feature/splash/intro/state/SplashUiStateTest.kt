package com.my.book.library.feature.splash.intro.state

import org.junit.Assert.assertTrue
import org.junit.Test

class SplashUiStateTest {

    @Test
    fun `기본 상태는 로딩 중이다`() {
        // Given: 아무 값도 넘기지 않고 기본 생성자로 상태를 만든다
        val state = SplashUiState()

        // When: 검증할 동작이 "생성" 자체이므로 별도 실행 단계는 없음

        // Then: 기본값의 isLoading이 true여야 한다
        assertTrue(state.isLoading)
    }
}
