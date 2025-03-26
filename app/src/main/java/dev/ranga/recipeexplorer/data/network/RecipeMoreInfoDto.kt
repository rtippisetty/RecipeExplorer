package dev.ranga.recipeexplorer.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeMoreInfoDto(
    val id: Int,
    val name: String,
    val description: String,
    val instructions: List<InstructionDto>,
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    @SerialName("cook_time_minutes") val cookTimeMinutes: Int = 0,
    @SerialName("prep_time_minutes") val prepTimeMinutes: Int = 0,
    @SerialName("total_time_minutes") val totalTimeMinutes: Int = 0,
    @SerialName("user_ratings") val userRatings: UserRatingsDto,
)

@Serializable
data class InstructionDto(
    val id: Int,
    @SerialName("position") val stepNumber: Int,
    @SerialName("display_text") val displayText: String,
)

@Serializable
data class UserRatingsDto(
    @SerialName("score") val score: Double,
)