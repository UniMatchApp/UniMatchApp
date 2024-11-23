package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class TokenResponseInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val newToken = response.header("New-Authorization-Token")
        newToken?.let {
            tokenProvider.saveToken(it)
        }

        return response
    }
}