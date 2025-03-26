package dev.ranga.recipeexplorer.domain

import dev.ranga.recipeexplorer.api.AddToFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.model.Recipe
import javax.inject.Inject

class AddToFavouriteRecipesUseCaseImpl @Inject constructor(
    private val recipeRepository: RecipeRepository
) : AddToFavouriteRecipesUseCase {
    override suspend fun add(recipe: Recipe): Boolean =
        recipeRepository.addFavouriteRecipe(recipe) != -1L
}