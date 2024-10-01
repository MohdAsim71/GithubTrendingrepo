package com.codinglance.githubtrendingrepo.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "repo_table")
data class DBData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val owner: String ="",
    val description: String
    )


data class Owner1(
    val avatar_url: String,
    val events_url: String,
    // Other fields...
)
