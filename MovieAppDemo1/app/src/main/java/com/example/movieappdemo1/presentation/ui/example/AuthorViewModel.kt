package com.example.movieappdemo1.presentation.ui.example

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappdemo1.common.log.LogUtil
import com.example.movieappdemo1.domain.usecase.SaveMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Post(val title: String, val content: String)
data class Author(val name: String)

val sampleAuthor = Author(name = "John Doe")
val samplePosts = listOf(
    Post(title = "First Post", content = "This is the content of the first post."),
    Post(title = "Second Post", content = "This is the content of the second post.")
)

@HiltViewModel
class AuthorViewModel @Inject constructor(
    private val saveMovieUseCase: SaveMovieUseCase
) : ViewModel() {
    private val _authorName = MutableLiveData("John Doe")
    val authorName: LiveData<String> = _authorName

    private val _posts = MutableLiveData(samplePosts)
    val posts: LiveData<List<Post>> = _posts

    init {
        viewModelScope.launch {
            delay(5000L)
            _authorName.value = "Name!!"
        }
    }
}

@Composable
fun AuthorColumn(viewModel: AuthorViewModel = hiltViewModel()) {
    val name by viewModel.authorName.observeAsState("")
    val posts by viewModel.posts.observeAsState(emptyList())

    AuthorColumnUI(name = name, posts = posts) {
        LogUtil.d_dev("Clicked")
    }
}

@Composable
fun AuthorColumnUI(
    name: String,
    posts: List<Post>,
    callback: () -> Unit
) {
    Column {
        NameLabel(name, callback)
        PostsList(posts)
    }
}

@Composable
fun NameLabel(name: String, callback: () -> Unit) {
    Text(text = "Author: $name",
        Modifier.clickable {
            callback()
        })
}

@Composable
fun PostsList(posts: List<Post>) {
    Column {
        posts.forEach { post ->
            Text(text = post.title)
            Text(text = post.content)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthorScreenPreview(
    name: String = sampleAuthor.name,
    posts: List<Post> = samplePosts
) {
    AuthorColumnUI(name = name, posts = posts) {

    }
}