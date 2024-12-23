package com.example.node.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.node.data.NewsData1
import com.example.node.repo.Frepository
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: Frepository) : ViewModel() {

    private val _news1 = MutableLiveData<List<NewsData1>>()
    val news1: LiveData<List<NewsData1>> = _news1

    fun uploadData(heading: String, description: String, report: String, imageUrl: String, tags: List<String>, timeStamp: Long) {
        viewModelScope.launch {
            repository.uploadData(heading, description, report, imageUrl, tags, timeStamp)
        }
    }

    fun uploadData2(heading1: String, description1: String, report1: String, imageUrl1: String, timeStamp: Long, heading2: String, description2: String, report2: String, imageUrl2: String) {
        viewModelScope.launch {
            repository.uploadData2(heading1, description1, report1, imageUrl1, timeStamp, heading2, description2, report2, imageUrl2)
        }
    }

    fun uploadData3(videoUrl: String, heading: String, imageUrl: String, timeStamp: Long) {
        viewModelScope.launch {
            repository.uploadData3(videoUrl, heading = heading, imageUrl = imageUrl, timeStamp =  timeStamp)
        }
    }

    fun uploadUserData(uuid: String) {
        viewModelScope.launch {
            repository.uploadUserID(uuid)
        }
    }

    init {
        fetchNewsFromFirestore()
    }

    private fun fetchNewsFromFirestore() {
        repository.fetchNewsFromFirestore().observeForever { news ->
            _news1.value = news
        }
    }

    fun deleteNews(newsId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            repository.deleteNews(newsId, onSuccess, onFailure)
        }
    }
}