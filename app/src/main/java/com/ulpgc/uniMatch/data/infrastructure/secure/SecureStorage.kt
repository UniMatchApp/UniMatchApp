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
        blockedUsers: List<String>
    ) {
        with(sharedPreferences.edit()) {
            putString("userId", userId)
            putString("email", email)
            putString("registrationDate", DateFormat.getDateInstance().format(registrationDate))
            putStringSet("blockedUsers", blockedUsers.toSet())
            apply()
        }
    }


    fun getUser(): User? {
        val userId = sharedPreferences.getString("userId", null)
        val email = sharedPreferences.getString("email", null)
        val registrationDate = sharedPreferences.getString("registrationDate", null)
        val blockedUsers = sharedPreferences.getStringSet("blockedUsers", emptySet())


        return if (userId != null && email != null && registrationDate != null) {
            User(
                userId,
                DateFormat.getDateInstance().parse(registrationDate),
                email,
                blockedUsers?.toList() ?: emptyList()
            )
        } else {
            null
        }
    }

    fun clearUser() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}