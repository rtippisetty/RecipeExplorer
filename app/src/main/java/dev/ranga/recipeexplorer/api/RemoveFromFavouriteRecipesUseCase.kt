package dev.ranga.recipeexplorer.api

import dev.ranga.recipeexplorer.api.model.Recipe

fun interface RemoveFromFavouriteRecipesUseCase {
    suspend fun remove(recipe: Recipe): Boolean
}