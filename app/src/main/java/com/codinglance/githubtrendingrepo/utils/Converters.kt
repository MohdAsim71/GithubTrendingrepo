package com.codinglance.githubtrendingrepo.utils

import androidx.room.TypeConverter
import com.codinglance.githubtrendingrepo.model.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromItemList(items: List<Item>?): String? {
        val gson = Gson()
        return gson.toJson(items)
    }

    @TypeConverter
    fun toItemList(data: String?): List<Item>? {
        val listType = object : TypeToken<List<Item>>() {}.type
        return Gson().fromJson(data, listType)
    }
}
