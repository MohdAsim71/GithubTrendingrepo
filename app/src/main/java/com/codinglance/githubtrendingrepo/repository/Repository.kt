package com.codinglance.githubtrendingrepo.repository

import com.codinglance.githubtrendingrepo.model.RepoResponse
import com.codinglance.githubtrendingrepo.network.ApiInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val  service: ApiInterface
)
{



    suspend fun getRepo(): RepoResponse {
        return service.getRepo("stars","stars")
    }
}