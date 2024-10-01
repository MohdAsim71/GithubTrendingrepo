package com.codinglance.githubtrendingrepo.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Migration from version 1 to version 2 - Adding new column 'owner'
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE repo_table ADD COLUMN owner TEXT NOT NULL DEFAULT ''")
    }
}


// Migration from version 2 to version 3 - Adding new column 'age'
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE repo_table ADD COLUMN owner2 TEXT NOT NULL DEFAULT ''")
    }
}