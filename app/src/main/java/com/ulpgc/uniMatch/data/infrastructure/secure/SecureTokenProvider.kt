package com.ulpgc.uniMatch.data.infrastructure.secure

import com.ulpgc.uniMatch.data.application.api.TokenProvider

class SecureTokenProvider(private val secureStorage: SecureStorage) : TokenProvider {
    override fun getToken(): String? {
        return secureStorage.getToken()
    }
    override fun saveToken(token: String) {
        secureStorage.saveToken(token)
    }

}