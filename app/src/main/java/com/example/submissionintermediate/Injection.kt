package com.example.submissionintermediate

import android.content.Context
import com.example.submissionintermediate.Api.ApiConfig
import com.example.submissionintermediate.Api.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}