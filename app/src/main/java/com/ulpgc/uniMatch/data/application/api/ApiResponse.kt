package com.ulpgc.uniMatch.data.application.api

data class ApiResponse<T>(
    val value: T?,
    val success: Boolean,
    val errorMessage: String?
) {
    companion object {
        fun <T> success(value: T): ApiResponse<T> {
            return ApiResponse(value, true, null)
        }

        fun <T> failure(errorMessage: String): ApiResponse<T> {
            return ApiResponse(null, false, errorMessage)
        }
    }
}
