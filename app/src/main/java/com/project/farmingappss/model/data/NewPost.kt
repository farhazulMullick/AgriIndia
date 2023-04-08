package com.project.farmingappss.model.data

data class NewPost(
    val userName: String,
    val id: String,
    val title: String,
    val description: String,
    val images: List<String>
)
