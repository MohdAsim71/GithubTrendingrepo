package com.codinglance.githubtrendingrepo

import com.codinglance.githubtrendingrepo.model.RepoResponse
import com.codinglance.githubtrendingrepo.network.ApiInterface
import com.codinglance.githubtrendingrepo.network.RetrofitModule
import com.codinglance.githubtrendingrepo.network.RetrofitModule.provideOkHttpClient
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

class APITest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiInterface: ApiInterface

    @Before
    fun setUp(){

        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Use the mock server URL for testing
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
        apiInterface = retrofit.create(ApiInterface::class.java)

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testApiInterfaceGetRepo() = runBlocking{
        // Prepare the mock response
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                {
                    "incomplete_results": false,
                    "items": [
                        {
                            "id": 1,
                            "name": "Test Repo",
                            "description": "This is a test repository"
                        }
                    ],
                    "total_count": 1
                }
                """
            )
        mockWebServer.enqueue(mockResponse)

        // Call the API
        val response= apiInterface.getRepo("stars", "stars")
        assertEquals(false, response.incomplete_results)
        assertEquals(1, response.total_count)
        assertEquals(1, response.items.size)
        assertEquals(1, response.items[0].id)
        assertEquals("Test Repo", response.items[0].name)
        assertEquals("This is a test repository", response.items[0].description)

    }

    @Test
    fun testGetRepoServerError() = runBlocking {
        // Mock the API response with a server error
        val mockResponse = MockResponse()
            .setResponseCode(500) // Server error
            .setBody("{\"error\": \"Internal Server Error\"}")
        mockWebServer.enqueue(mockResponse)

        // Call the API method and handle the response
        try {
            val response = apiInterface.getRepo("stars", "stars")
            // If no exception is thrown, fail the test
            assert(false) { "Expected an exception due to server error" }
        } catch (e: Exception) {
            // Verify that the exception is handled
            assert(e is retrofit2.HttpException) {
                "Expected HttpException but got ${e::class.simpleName}"
            }
        }
    }

    @Test
    fun testGetRepoNetworkError() = runBlocking {
        // Mock the API response with a network error
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))

        // Call the API method and handle the response
        try {
            val response = apiInterface.getRepo("stars", "stars")
            // If no exception is thrown, fail the test
            assert(false) { "Expected an exception due to network error" }
        } catch (e: IOException) {
            // Verify that the exception is handled
            assert(e is IOException) {
                "Expected IOException but got ${e::class.simpleName}"
            }
        }
    }

    @Test
    fun testGetRepoClientError() = runBlocking {
        // Mock the API response with a client error
        val mockResponse = MockResponse()
            .setResponseCode(400) // Client error
            .setBody("{\"error\": \"Bad Request\"}")
        mockWebServer.enqueue(mockResponse)

        // Call the API method and handle the response
        try {
            val response = apiInterface.getRepo("stars", "stars")
            // If no exception is thrown, fail the test
            assert(false) { "Expected an exception due to client error" }
        } catch (e: retrofit2.HttpException) {
            // Verify that the exception is handled
            assert(e.code() == 400) {
                "Expected HTTP status code 400 but got ${e.code()}"
            }
        }
    }


}