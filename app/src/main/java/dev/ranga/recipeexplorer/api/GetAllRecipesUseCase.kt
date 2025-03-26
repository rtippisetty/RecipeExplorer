package dev.ranga.recipeexplorer.api

import dev.ranga.recipeexplorer.api.model.Recipe

fun interface GetAllRecipesUseCase {

    suspend fun allRecipes(
        from: Int,
        size: Int,
    ): List<Recipe>
}