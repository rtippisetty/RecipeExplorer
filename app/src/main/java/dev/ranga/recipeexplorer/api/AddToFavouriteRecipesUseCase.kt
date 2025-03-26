package dev.ranga.recipeexplorer.api

import dev.ranga.recipeexplorer.api.model.Recipe

fun interface AddToFavouriteRecipesUseCase {
    suspend fun add(recipe: Recipe): Boolean
}