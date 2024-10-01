package com.codinglance.githubtrendingrepo

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.codinglance.githubtrendingrepo.db.MIGRATION_1_2
import com.codinglance.githubtrendingrepo.db.MIGRATION_2_3
import com.codinglance.githubtrendingrepo.db.UserDatabase
import com.codinglance.githubtrendingrepo.db.repoDao
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class MigrationTest {
    private val TEST_DB = "migration-test"

    private lateinit var db: UserDatabase
    private lateinit var userDao: repoDao
    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        UserDatabase::class.java,  // Use the class reference, not the class name
        emptyList(),  // If no custom migration specs are provided
        FrameworkSQLiteOpenHelperFactory()
    )


    @Before
    fun setup() {
        // Initialize the in-memory database before each test
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).build()

        // Get reference to UserDao
        userDao = db.userDao()
    }

    @After
    fun teardown() {
        // Close the database after each test
        db.close()
    }


    @Test
    fun migrate1To2() {
        // Create the database in version 1
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL("INSERT INTO repo_table (id, name,description) VALUES (1, 'John Doe','john' )")
            close()
        }

        var db: SupportSQLiteDatabase = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)


        // Verify the 'age' column has been added and data is preserved
        db.query("SELECT id, name, description, owner FROM repo_table WHERE id = 1").use { cursor ->
            assert(cursor.moveToFirst())
            assert(cursor.getInt(0) == 1)  // id
            assert(cursor.getString(1) == "John Doe")  // name
            assert(cursor.getString(2) == "john")
         //   assert((cursor.isNull(3)))// owner should be NULL since we did not provide a value
        }
    }


    @Test
    @Throws(IOException::class)
    fun migrate2To3() {
        // Create the database in version 2
        helper.createDatabase(TEST_DB, 2).apply {
            // Insert some test data
            execSQL("INSERT INTO repo_table (id, name, description) VALUES (1, 'John Doe', 'description')")
            close()
        }
        val db: SupportSQLiteDatabase = helper.runMigrationsAndValidate(TEST_DB, 3, true, MIGRATION_2_3)

        // Verify the 'addresses' table has been created
        db.query("SELECT * FROM owner").use { cursor ->
            assert(cursor.columnCount == 3)  // Verify the columns: id, userId, address
        }

        // Ensure the data in 'repo_table' table is still correct
        db.query("SELECT id, name, age FROM repo_table WHERE id = 1").use { cursor ->
            assert(cursor.moveToFirst())
            assert(cursor.getInt(0) == 1)  // id
            assert(cursor.getString(1) == "John Doe")  // name
            assert(cursor.getInt(2) == 25)  // age
        }
    }
}

