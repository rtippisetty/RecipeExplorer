package dev.ranga.recipeexplorer.api.model

data class Recipe(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val totalTimeMinutes: Int,
    val userRatings: UserRatings,
)
