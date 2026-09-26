package com.my.book.library.data.repository.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.my.book.library.core.model.req.ReqLoanItemSrchByLib
import com.my.book.library.core.model.res.ResLoanItemSrchByLib
import com.my.book.library.data.api.ApiService
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class GetLoanItemSrchByLibPagingSource(
    private val apiService: ApiService,
    private val authToken: String,
    private val format: String,
    private val reqLoanItemSrchByLib: ReqLoanItemSrchByLib
): PagingSource<Int, ResLoanItemSrchByLib.ResponseData.DocWrapper>() {

    override fun getRefreshKey(state: PagingState<Int, ResLoanItemSrchByLib.ResponseData.DocWrapper>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResLoanItemSrchByLib.ResponseData.DocWrapper> {
        return try {
            val position = params.key ?: STARTING_PAGE_INDEX

            val response = apiService.getLoanItemSrchByLib(
                authKey = authToken,
                region = reqLoanItemSrchByLib.region,
                dtlRegion = reqLoanItemSrchByLib.dtlRegion,
                pageNo = position,
                pageSize = reqLoanItemSrchByLib.pageSize,
                format = format
            )

            val docs = response.body()?.response?.docs

            if (docs != null) {
                LoadResult.Page(
                    data = docs,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (docs.isEmpty()) null else position + 1,
                    itemsBefore = 0,
                    itemsAfter = 0
                )
            } else {
                throw Exception("PagingSource Error")
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
