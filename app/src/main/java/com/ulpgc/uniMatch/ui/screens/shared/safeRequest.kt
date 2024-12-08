package com.ulpgc.uniMatch.ui.screens.shared

import android.util.Log
import com.google.gson.JsonSyntaxException
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <T> safeRequest(
    block: () -> T
): Result<T> {
    val stackTrace = Thread.currentThread().stackTrace
    val callerElement = stackTrace.getOrNull(3)
    val fullClassName = callerElement?.className ?: "UnknownClass"
    val methodName = callerElement?.methodName ?: "UnknownMethod"
    val simpleClassName = fullClassName.substringAfterLast('.')

    return try {
        Result.success(block())
    } catch (e: HttpException) {
        val errorMessage = e.response()?.errorBody()?.string() ?: "Unknown error occurred"
        Log.e("$simpleClassName::$methodName", "HTTP Exception: $errorMessage")
        Result.failure(Exception("HTTP Exception: $errorMessage"))
    } catch (e: SocketTimeoutException) {
        Log.e("$simpleClassName::$methodName", "Socket Timeout Exception: ${e.message}")
        Result.failure(Exception("Socket timeout: ${e.message}"))
    } catch (e: UnknownHostException) {
        Log.e("$simpleClassName::$methodName", "UnknownHostException: ${e.message}")
        Result.failure(Exception("Unknown host: ${e.message}"))
    } catch (e: JSONException) {
        Log.e("$simpleClassName::$methodName", "JSON Exception: ${e.message}")
        Result.failure(Exception("JSON error: ${e.message}"))
    } catch (e: JsonSyntaxException) {
        Log.e("$simpleClassName::$methodName", "JSON Syntax Exception: ${e.message}")
        Result.failure(Exception("JSON syntax error: ${e.message}"))
    } catch (e: Exception) {
        Log.e("$simpleClassName::$methodName", "Exception: ${e.message}")
        Result.failure(Exception("General error: ${e.message}"))
    }
}

