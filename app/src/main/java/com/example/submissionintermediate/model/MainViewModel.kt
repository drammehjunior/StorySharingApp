package com.example.submissionintermediate.model

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submissionintermediate.Api.StoryRepository
import com.example.submissionintermediate.Injection
import com.example.submissionintermediate.MainActivity
import com.example.submissionintermediate.pojo.ListStoryItem


class MainViewModel(storyRepository: StoryRepository, token: String): ViewModel() {
//    private val _listStories = MutableLiveData<List<ListStoryItem>>()
//    val listStories: LiveData<List<ListStoryItem>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var tokenView = ""



    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory(token).cachedIn(viewModelScope)



    companion object{
        private const val TAG = "MainViewModel"
    }
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context), "Bearer ${MainActivity.tokenActivity}") as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}