package dev.ranga.recipeexplorer.api.model

data class RecipeDetail(
    val id: Long,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val totalTimeMinutes: Long,
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
    thumbnailUrl = thumbnailUrl,
    totalTimeMinutes = totalTimeMinutes,
    userRatings = userRatings
)
