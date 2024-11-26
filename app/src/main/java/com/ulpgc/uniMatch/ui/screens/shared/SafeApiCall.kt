package com.ulpgc.uniMatch.ui.screens.shared

import com.ulpgc.uniMatch.data.application.api.ApiResponse

inline fun <T> safeApiCall(
    call: () -> ApiResponse<T>
): Result<T> {
    return safeRequest {
        val response = call()
        if (response.success) {
            response.value?.let { Result.success(it) }
                ?: Result.failure(Throwable("Response value is null"))
        } else {
            Result.failure(Throwable(response.errorMessage ?: "Unknown error occurred"))
        }
    }
}