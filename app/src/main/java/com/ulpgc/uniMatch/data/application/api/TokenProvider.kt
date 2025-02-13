package com.ulpgc.uniMatch.data.application.api

interface TokenProvider {
    fun getToken(): String?
    fun saveToken(token: String)
}
