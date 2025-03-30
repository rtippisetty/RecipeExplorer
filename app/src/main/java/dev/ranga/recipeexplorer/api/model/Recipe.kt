package dev.ranga.recipeexplorer.api.model

data class Recipe(
    val id: Long,
    val name: String,
    val thumbnailUrl: String,
    val totalTimeMinutes: Long,
    val userRatings: UserRatings,
)
