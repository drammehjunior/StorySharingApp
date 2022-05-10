package com.example.submissionintermediate.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.submissionintermediate.Api.ApiService
import com.example.submissionintermediate.pojo.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class StoryPagingSource(private val apiService: ApiService, private val token: String) : PagingSource<Int, ListStoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            withContext(Dispatchers.IO){

            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(token, position, params.loadSize)

            val tempData = responseData.awaitResponse().body()?.listStory



            LoadResult.Page(
                data = tempData!!,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (tempData.isNullOrEmpty()) null else position + 1
            )}
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}