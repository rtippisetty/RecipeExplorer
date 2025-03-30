package dev.ranga.recipeexplorer.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeListResponseDto(
    val count: Long,
    val results: List<RecipeDto>
)

@Serializable
data class RecipeDto(
    val id: Long,
    val name: String? = "",
    @SerialName("total_time_minutes") val totalTimeMinutes: Long? = 0,
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    @SerialName("user_ratings") val userRatings: UserRatingsDto? = null,
)
