package com.codinglance.githubtrendingrepo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.codinglance.githubtrendingrepo.model.DBData
import com.codinglance.githubtrendingrepo.model.RepoResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface repoDao {
    @Insert
    suspend fun insertUser(user: List<DBData>)

}