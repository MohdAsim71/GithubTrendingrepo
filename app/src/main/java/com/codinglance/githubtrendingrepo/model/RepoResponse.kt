package com.codinglance.githubtrendingrepo.model

data class RepoResponse(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)