package com.my.book.library.feature.main.ui.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.my.book.library.core.common.util.LogUtil
import com.my.book.library.core.resource.R

/**
 * 도서 표지 비동기 이미지 — 로드 실패 시 Inside, 성공 시 Crop으로 표시 방식을 전환한다.
 * 홈 화면(HotTrendBookItem/PopularLoanBookRow)과 BookRank 화면에서 공통으로 사용한다.
 */
@Composable
internal fun BookCoverImage(
    imageUrl: String?,
    modifier: Modifier,
    cornerRadius: Dp
) {
    var contentScale by remember {
        mutableStateOf<ContentScale>(ContentScale.Inside)
    }

    AsyncImage(
        modifier = modifier.clip(RoundedCornerShape(cornerRadius)),
        model = imageUrl,
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = contentScale,
        placeholder = painterResource(R.drawable.ic_book_grey_57x64),
        error = painterResource(R.drawable.ic_book_grey_57x64),
        onError = { error ->
            contentScale = ContentScale.Inside
            LogUtil.e_dev("book image load error: ${error.result.throwable}")
        },
        onSuccess = {
            contentScale = ContentScale.Crop
            LogUtil.d_dev("book image success, url: $imageUrl")
        }
    )
}
