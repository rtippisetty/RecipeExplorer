package dev.ranga.recipeexplorer.ui.home

import dev.ranga.recipeexplorer.api.model.Recipe

sealed interface RecipesState {
    data class Success(val recipes: List<Recipe>) : RecipesState
    object Loading : RecipesState
    data class Error(val message: String) : RecipesState
}