package dev.ranga.recipeexplorer.domain

import dev.ranga.recipeexplorer.api.model.Recipe
import dev.ranga.recipeexplorer.api.model.RecipeDetail
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    //Network
    suspend fun getAllRecipes(from: Int, size: Int): List<Recipe>
    suspend fun getRecipeDetail(recipeId: Long): RecipeDetail

    //Db
    fun getFavouriteRecipes(): Flow<List<Recipe>>
    suspend fun addFavouriteRecipe(recipe: Recipe): Long
    suspend fun removeFavouriteRecipe(recipe: Recipe): Int
}