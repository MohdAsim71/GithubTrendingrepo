package com.codinglance.githubtrendingrepo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codinglance.githubtrendingrepo.model.DBData
import com.codinglance.githubtrendingrepo.model.RepoResponse
import com.codinglance.githubtrendingrepo.utils.Converters

@Database(entities = [DBData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // Add this line
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): repoDao
}


