package dev.ranga.recipeexplorer.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeListResponseDto(
    val count: Int,
    val results: List<RecipeDto>
)

@Serializable
data class RecipeDto(
    val id: Int,
    val name: String? = "",
    val description: String? = "",
    @SerialName("total_time_minutes") val totalTimeMinutes: Int? = 0,
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    @SerialName("user_ratings") val userRatings: UserRatingsDto? = null,
)
