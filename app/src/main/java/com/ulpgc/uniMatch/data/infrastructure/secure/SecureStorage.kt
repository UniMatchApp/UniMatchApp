package com.ulpgc.uniMatch.data.infrastructure.secure

import android.content.Context
import android.icu.text.DateFormat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.ulpgc.uniMatch.data.domain.models.User
import java.util.Date

class SecureStorage(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "user_secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveUser(
        userId: String,
        email: String,
        registrationDate: Date,
        blockedUsers: List<String>,
        reportedUsers: List<String>,
        registered: Boolean,
        token: String
    ) {
        with(sharedPreferences.edit()) {
            putString("userId", userId)
            putString("email", email)
            putString("registrationDate", DateFormat.getDateInstance().format(registrationDate))
            putStringSet("blockedUsers", blockedUsers.toSet())
            putStringSet("reportedUsers", reportedUsers.toSet())
            putBoolean("registered", registered)
            putString("token", token)
            apply()
        }
    }


    fun getUser(): User? {
        val userId = sharedPreferences.getString("userId", null)
        val email = sharedPreferences.getString("email", null)
        val registrationDate = sharedPreferences.getString("registrationDate", null)
        val reportedUsers = sharedPreferences.getStringSet("reportedUsers", emptySet())
        val blockedUsers = sharedPreferences.getStringSet("blockedUsers", emptySet())
        val registered = sharedPreferences.getBoolean("registered", false)


        return if (userId != null && email != null && registrationDate != null) {
            User(
                userId,
                DateFormat.getDateInstance().parse(registrationDate),
                email,
                blockedUsers?.toList() ?: emptyList(),
                reportedUsers?.toList() ?: emptyList(),
                registered
            )
        } else {
            null
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("token", token)
            apply()
        }
    }

    fun clearUser() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}