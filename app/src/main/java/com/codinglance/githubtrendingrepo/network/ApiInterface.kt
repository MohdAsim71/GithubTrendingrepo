package com.codinglance.githubtrendingrepo.network
import com.codinglance.githubtrendingrepo.model.RepoResponse
import okhttp3.MultipartBody
import retrofit2.http.*


interface ApiInterface
{
    companion object{
        const val BASE_URL="https://api.github.com/search/"
    }


    @GET("repositories")
   suspend fun getRepo(
        @Query("q") q: String,
        @Query("sort") sort: String
    ): RepoResponse

}