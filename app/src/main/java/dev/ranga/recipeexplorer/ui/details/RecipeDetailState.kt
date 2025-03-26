package dev.ranga.recipeexplorer.ui.details

import dev.ranga.recipeexplorer.api.model.RecipeDetail

sealed interface RecipeDetailState {
    object Loading : RecipeDetailState
    data class Success(val recipeDetail: RecipeDetail) : RecipeDetailState
    data class Error(val message: String?) : RecipeDetailState
}