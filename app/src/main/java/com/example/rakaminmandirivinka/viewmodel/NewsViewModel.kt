package com.example.rakaminmandirivinka.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rakaminmandirivinka.model.Article
import com.example.rakaminmandirivinka.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    private val newsRepository = NewsRepository()

    init {
        fetchTopHeadlines()
    }

    private fun fetchTopHeadlines() {
        viewModelScope.launch {
            try {
                val response = newsRepository.getTopHeadlines()
                if (response.isSuccessful && response.body() != null) {
                    _articles.value = response.body()!!.articles
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    fun fetchAllNews(query: String) {
        viewModelScope.launch {
            try {
                val response = newsRepository.getAllNews(query)
                if (response.isSuccessful && response.body() != null) {
                    _articles.value = response.body()!!.articles
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }
}
