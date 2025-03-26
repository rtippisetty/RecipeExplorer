package dev.ranga.recipeexplorer.api.model

data class RecipeDetail(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val cookTimeMinutes: Int,
    val prepTimeMinutes: Int,
    val totalTimeMinutes: Int,
    val instructions: List<Instruction>,
    val userRatings: UserRatings
)

data class Instruction(
    val stepNumber: Int,
    val displayText: String
)

data class UserRatings(
    val score: Double
)

fun RecipeDetail.toRecipe() = Recipe(
    id = id,
    name = name,
    description = description,
    thumbnailUrl = thumbnailUrl,
    totalTimeMinutes = totalTimeMinutes
)
