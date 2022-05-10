package com.example.submissionintermediate.Api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.submissionintermediate.paging.StoryPagingSource
import com.example.submissionintermediate.pojo.ListStoryItem

class StoryRepository(private val apiService: ApiService) {
    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        Log.d("TOKEN", token)

        return Pager(
            config = PagingConfig(
                pageSize = 1
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }
}