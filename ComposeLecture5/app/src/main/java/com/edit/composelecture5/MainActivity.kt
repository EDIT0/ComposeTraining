package com.edit.composelecture5

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edit.composelecture5.compose.TvShowListItem
import com.edit.composelecture5.data.TvShowList
import com.edit.composelecture5.model.TvShow

class MainActivity : ComponentActivity() {

    private val clickListener : (String) -> (Unit) = {
        Toast.makeText(this, "${it}", Toast.LENGTH_SHORT).show()
    }

    private val tvShowClickListener : (TvShow) -> (Unit) = {
        startActivity(InfoActivity.intent(this, it))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            ScrollableColumnDemo()
//            LazyColumnDemo()
//            LazyColumnDemo2 {
//                Toast.makeText(this, "${it}", Toast.LENGTH_SHORT).show()
//            }
//            LazyColumnDemo2(clickListener)
            DisplayTvShows(selectedItem = tvShowClickListener)
        }
    }
}

@Composable
fun ScrollableColumnDemo() {
    val scrollState = rememberScrollState()


    Column (
        modifier = Modifier
            .verticalScroll(scrollState)
            ){
        for(i in 1..100) {
            Text(
                "User Name ${i}",
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .padding(10.dp)
            )
            Divider(
                color = Color.Black,
                thickness = 5.dp
            )
        }
    }
}

@Composable
fun LazyColumnDemo() {
    LazyColumn {
        items(
            count = 100
        ) {
            Text(
                "User Name ${it}",
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .padding(10.dp)
            )
            Divider(
                color = Color.Black,
                thickness = 5.dp
            )
        }
    }
}

@Composable
fun LazyColumnDemo2(selectedItem: (String) -> (Unit)) {
    LazyColumn {
        items(
            count = 100
        ) {
            Log.i("MYTAG", "${it}")
            Text(
                "User Name ${it}",
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        selectedItem("${it} Selected")
                    }
            )
            Divider(
                color = Color.Black,
                thickness = 5.dp
            )
        }
    }
}

@Composable
fun DisplayTvShows(selectedItem: (TvShow) -> (Unit)) {
    val tvShows = remember {
        TvShowList.tvShows
    }

    LazyColumn(
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        )
    ) {
        items(
            items = tvShows,
            itemContent = {
                TvShowListItem(tvShow = it, selectedItem = selectedItem)
            }
        )
    }
}