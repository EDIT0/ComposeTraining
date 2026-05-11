package com.my.book.library.data.repository.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.my.book.library.core.model.req.ReqSearchBookHoldingLibrary
import com.my.book.library.core.model.res.ResSearchBookHoldingLibrary
import com.my.book.library.data.api.ApiService
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class GetSearchBookHoldingLibraryPagingSource(
    private val apiService: ApiService,
    private val authToken: String,
    private val format: String,
    private val reqSearchBookHoldingLibrary: ReqSearchBookHoldingLibrary
): PagingSource<Int, ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper>() {

    override fun getRefreshKey(state: PagingState<Int, ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResSearchBookHoldingLibrary.ResponseData.LibraryWrapper> {
        return try {
            val position = params.key ?: STARTING_PAGE_INDEX

            val response = apiService.getSearchBookHoldingLibrary(
                authKey = authToken,
                isbn = reqSearchBookHoldingLibrary.isbn,
                region = reqSearchBookHoldingLibrary.region,
                dtlRegion = reqSearchBookHoldingLibrary.dtlRegion,
                format = format,
                pageNo = position,
                pageSize = reqSearchBookHoldingLibrary.pageSize
            )

            val post = response.body()

            post?.let {
                LoadResult.Page(
                    data = post.response.libs,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (post.response.libs.isEmpty()) null else position + 1,
                    itemsBefore = 0,
                    itemsAfter = 0
                )
            } ?: throw Exception("PagingSource Error")

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
