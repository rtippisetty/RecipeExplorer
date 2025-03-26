package dev.ranga.recipeexplorer.api

import dev.ranga.recipeexplorer.api.model.Recipe
import kotlinx.coroutines.flow.Flow

fun interface GetFavouriteRecipesUseCase {
    fun favourites(): Flow<List<Recipe>>
}