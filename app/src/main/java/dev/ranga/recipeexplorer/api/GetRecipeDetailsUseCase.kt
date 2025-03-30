package dev.ranga.recipeexplorer.api

import dev.ranga.recipeexplorer.api.model.RecipeDetail

fun interface GetRecipeDetailsUseCase {
    suspend fun details(id: Long): RecipeDetail
}