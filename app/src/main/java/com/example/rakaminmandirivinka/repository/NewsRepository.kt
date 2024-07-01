package com.example.rakaminmandirivinka.repository

import com.example.rakaminmandirivinka.model.NewsResponse
import com.example.rakaminmandirivinka.network.NewsInterface
import com.example.rakaminmandirivinka.network.RetrofitClient
import retrofit2.Response

class NewsRepository {
    private val newsApi: NewsInterface = RetrofitClient.instance.create(NewsInterface::class.java)

    suspend fun getTopHeadlines(): Response<NewsResponse> {
        return newsApi.getTopHeadlines()
    }

    suspend fun getAllNews(query: String): Response<NewsResponse> {
        return newsApi.getAllNews(query)
    }
}
