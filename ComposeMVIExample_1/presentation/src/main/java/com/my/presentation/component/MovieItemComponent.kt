package com.my.presentation.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.my.data.BuildConfig
import com.my.domain.model.MovieModelResult
import com.my.presentation.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MovieItem(
    index: Int,
    movieModelResult: MovieModelResult,
    onItemClick: (MovieModelResult) -> Unit,
    onItemLongClick: (MovieModelResult) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
//            .clickable {
//                LogUtil.d_dev("Clicked title: ${movieModelResult.title}")
//                onItemClick.invoke(movieModelResult)
//            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        Log.d("MYTA", "Clicked title: ${movieModelResult.title}")
                        onItemClick.invoke(movieModelResult)
                    },
                    onLongPress = {
                        onItemLongClick.invoke(movieModelResult)
                    }
                )
            }
    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
                    .padding(5.dp)
                    .background(White),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                GlideImage(
                    imageModel = { BuildConfig.BASE_MOVIE_POSTER + movieModelResult.posterPath },
//                    previewPlaceholder = painterResource(id = R.drawable.),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.FillHeight
                    )
                )
            }
            Column(
                modifier = Modifier
                    .weight(2f)
                    .background(White)
            ) {
                Text(
                    text = "${index} / ${movieModelResult.title}",
                    modifier = Modifier
                        .padding(5.dp),
                    maxLines = 3
                )

                Text(
                    text = "${movieModelResult.releaseDate}",
                    modifier = Modifier
                        .padding(5.dp),
                    maxLines = 1
                )
            }
        }
    }
}