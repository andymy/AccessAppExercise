package com.andy.access.api

import com.andy.access.model.User
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GithubService {

    @GET("users")
    fun getUsers(
        @Query("since") since: Int = 0,
        @Query("per_page") per_page: Int = 100
    ): Single<Response<List<User>>>

    companion object {

        private const val API = "https://api.github.com/"

        private const val CONNECT_TIME_OUT: Long = 10
        private const val READ_TIME_OUT: Long = 10
        private const val WRITE_TIME_OUT: Long = 10

        private val okHttpClient = OkHttpClient.Builder()
            .apply {
                connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            }.build()

        fun create(): GithubService = Retrofit.Builder()
            .apply {
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                addConverterFactory(GsonConverterFactory.create())
                baseUrl(API)
                client(okHttpClient)
            }
            .build()
            .create(GithubService::class.java)
    }
}