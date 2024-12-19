package com.ulpgc.uniMatch.ui.screens.shared

import com.ulpgc.uniMatch.data.application.api.ApiResponse

inline fun <T> safeApiCall(
    call: () -> ApiResponse<T>
): Result<T?> {
    return safeRequest {
        val response = call()
        if (!response.success) {
            throw Exception(response.errorMessage ?: "Unknown error occurred")
        }
        response.value
    }
}
