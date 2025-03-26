package dev.ranga.recipeexplorer.data.mapper

import dev.ranga.recipeexplorer.api.model.Instruction
import dev.ranga.recipeexplorer.api.model.Recipe
import dev.ranga.recipeexplorer.api.model.RecipeDetail
import dev.ranga.recipeexplorer.api.model.UserRatings
import dev.ranga.recipeexplorer.data.local.FavouriteRecipeEntity
import dev.ranga.recipeexplorer.data.network.InstructionDto
import dev.ranga.recipeexplorer.data.network.RecipeDto
import dev.ranga.recipeexplorer.data.network.RecipeMoreInfoDto
import dev.ranga.recipeexplorer.data.network.UserRatingsDto
import java.net.URL

internal fun Recipe.toFavouriteRecipeEntity(): FavouriteRecipeEntity = FavouriteRecipeEntity(
    id = id,
    name = name,
    description = description,
    thumbnailUrl = thumbnailUrl,
    totalTimeMinutes = totalTimeMinutes
)

internal fun RecipeDto.toRecipe(): Recipe {
    val url = tryParseUrl(thumbnailUrl)
    return Recipe(
        id = id,
        name = name ?: "",
        description = description ?: "",
        thumbnailUrl = url,
        totalTimeMinutes = totalTimeMinutes ?: 0
    )
}

internal fun FavouriteRecipeEntity.toRecipe(): Recipe {
    val url = tryParseUrl(thumbnailUrl)
    return Recipe(
        id = id,
        name = name,
        description = description,
        thumbnailUrl = url,
        totalTimeMinutes = totalTimeMinutes
    )
}

internal fun RecipeMoreInfoDto.toRecipeDetails(): RecipeDetail {
    val url = tryParseUrl(thumbnailUrl)
    return RecipeDetail(
        id = id,
        name = name,
        description = description,
        thumbnailUrl = url,
        totalTimeMinutes = totalTimeMinutes,
        cookTimeMinutes = cookTimeMinutes,
        prepTimeMinutes = prepTimeMinutes,
        instructions = instructions.map {
            it.toInstruction()
        },
        userRatings = userRatings.toUserRatings()
    )
}

internal fun InstructionDto.toInstruction() = Instruction(
    stepNumber = stepNumber,
    displayText = displayText
)

internal fun UserRatingsDto.toUserRatings() = UserRatings(
    score = score
)

private fun tryParseUrl(url: String): String = try {
    URL(url).toString()
} catch (e: Exception) {
    throw IllegalStateException("Invalid thumbnail URL: ${url}", e)
}