package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.TokenProvider
import com.ulpgc.uniMatch.data.infrastructure.viewModels.UserViewModel
import okhttp3.Interceptor
import okhttp3.Response

class TokenResponseInterceptor(
    private val tokenProvider: TokenProvider,
    private val userViewModel: () -> UserViewModel
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.code == 401) {
            userViewModel.invoke().logout()
            return response
        }

        val newToken = response.header("Authorization")
        newToken?.let {
            val tokenParts = it.split(" ")
            tokenProvider.saveToken(tokenParts[1])
        }

        return response
    }
}