package com.codinglance.githubtrendingrepo.repository

import com.codinglance.githubtrendingrepo.db.repoDao
import com.codinglance.githubtrendingrepo.model.DBData
import com.codinglance.githubtrendingrepo.model.RepoResponse
import com.codinglance.githubtrendingrepo.network.ApiInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val  service: ApiInterface,
    private val userDao: repoDao
)
{


    suspend fun insert(user: List<DBData>) {
        userDao.insertUser(user)
    }

    suspend fun getRepo(): RepoResponse {
        return service.getRepo("stars","stars")
    }
}