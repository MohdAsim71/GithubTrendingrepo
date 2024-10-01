package com.codinglance.githubtrendingrepo

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.codinglance.githubtrendingrepo.db.MIGRATION_1_2
import com.codinglance.githubtrendingrepo.db.UserDatabase
import com.codinglance.githubtrendingrepo.db.repoDao
import com.codinglance.githubtrendingrepo.model.DBData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class repoDAOTest {

    private val TEST_DB = "migration-test"

    private lateinit var db: UserDatabase
    private lateinit var repoDao: repoDao



    @Before
    fun setup() {
        // Initialize the in-memory database before each test
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).build()

        // Get reference to UserDao
        repoDao = db.userDao()
    }

    @After
    fun teardown() {
        // Close the database after each test
        db.close()
    }

    @Test
    fun testInsertDao() = runBlocking{
        val mockData = DBData(1,"user","user data","owner")
        repoDao.insertUser(listOf(mockData))

       val retriveUser= repoDao.getUserById(1)

        assertNotNull(retriveUser)
        assertEquals("user", retriveUser?.name)
        assertEquals("user data", retriveUser?.description)

    }

    @Test
    fun getAllUsers() = runBlocking {
        // Given two users
        val user1 = DBData(id = 1, name = "John", description = "john data", owner = "owner")
        val user2 = DBData(id = 2, name = "Jane", description = "jane data",owner = "owner")

        // When inserting the users
        repoDao.insertUser(listOf( user1))
        repoDao.insertUser(listOf( user2))

        // Then retrieve all users and assert they match the inserted users
        val userList = repoDao.getAllUsers()
        assertEquals(2, userList.size)
        assertEquals("John", userList[0].name)
        assertEquals("Jane", userList[1].name)
    }



}