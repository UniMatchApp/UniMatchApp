package com.ulpgc.uniMatch.data.infrastructure.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ulpgc.uniMatch.data.domain.models.Location
import com.ulpgc.uniMatch.data.domain.models.Survey

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromListToString(value: List<String>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromStringToList(value: String?): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromSurveyList(surveys: List<Survey>): String {
        return Gson().toJson(surveys)
    }

    @TypeConverter
    fun toSurveyList(surveysString: String): List<Survey> {
        return Gson().fromJson(surveysString, Array<Survey>::class.java).toList()
    }

}