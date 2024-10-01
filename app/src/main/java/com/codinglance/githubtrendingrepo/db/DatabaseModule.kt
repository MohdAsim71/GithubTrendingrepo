package com.codinglance.githubtrendingrepo.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "repo_database"
        )
            .build()
    }

    @Provides
    fun provideUserDao(database: UserDatabase): repoDao {
        return database.userDao()
    }
}
