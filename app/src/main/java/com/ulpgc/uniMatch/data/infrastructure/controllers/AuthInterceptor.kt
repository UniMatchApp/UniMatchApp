package com.ulpgc.uniMatch.data.infrastructure.controllers

import com.ulpgc.uniMatch.data.application.api.TokenProvider
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

/*
class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getToken() ?: ""

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}
*/

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getToken() ?: ""
        val fcmtoken = tokenProvider.getFCMToken() ?: ""

        val originalRequest = chain.request()
        val originalBody = originalRequest.body

        val newRequestBody = if (originalBody != null) {
            val mediaType = originalBody.contentType()
            val originalJson = originalBody.toString()
            val jsonObject = JSONObject(originalJson)

            jsonObject.put("fcmtoken", fcmtoken)

            val newJson = jsonObject.toString()

            newJson.toRequestBody(mediaType)
        } else {
            null
        }

        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .method(originalRequest.method, newRequestBody)
            .build()

        return chain.proceed(newRequest)
    }
}
