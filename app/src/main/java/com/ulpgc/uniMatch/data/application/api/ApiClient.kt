package com.ulpgc.uniMatch.data.application.api

import NotificationPayload
import com.google.gson.GsonBuilder
import com.ulpgc.uniMatch.BuildConfig
import com.ulpgc.uniMatch.data.domain.models.notification.NotificationPayload
import com.ulpgc.uniMatch.data.infrastructure.controllers.AuthInterceptor
import com.ulpgc.uniMatch.data.infrastructure.controllers.TokenResponseInterceptor
import com.ulpgc.uniMatch.data.infrastructure.entities.NotificationPayloadAdapter
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL + "api/v1/") // URL base de la API
            .addConverterFactory(GsonConverterFactory.create()) // Usando Gson para la conversión de JSON
            .build()
    }
}


