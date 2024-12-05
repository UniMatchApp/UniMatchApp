package com.ulpgc.uniMatch.data.application.api

import NotificationPayload
import com.google.gson.GsonBuilder
import com.ulpgc.uniMatch.BuildConfig
import com.ulpgc.uniMatch.data.infrastructure.controllers.AuthInterceptor
import com.ulpgc.uniMatch.data.infrastructure.controllers.TokenResponseInterceptor
import com.ulpgc.uniMatch.data.infrastructure.entities.NotificationPayloadAdapter
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient(
    private val tokenProvider: TokenProvider,
    private val userViewModelProvider: () -> UserViewModel
) {

    private val BASE_URL: String = BuildConfig.BASE_URL + "api/v1/"

    private val gson = GsonBuilder()
        .registerTypeAdapter(NotificationPayload::class.java, NotificationPayloadAdapter())
        .create()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenProvider))
        .addInterceptor(TokenResponseInterceptor(tokenProvider) {
            userViewModelProvider()
        })
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}
