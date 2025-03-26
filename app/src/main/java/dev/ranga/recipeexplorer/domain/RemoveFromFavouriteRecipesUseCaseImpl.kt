package dev.ranga.recipeexplorer.domain

import dev.ranga.recipeexplorer.api.RemoveFromFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.model.Recipe
import javax.inject.Inject

class RemoveFromFavouriteRecipesUseCaseImpl @Inject constructor(
    private val recipeRepository: RecipeRepository
) : RemoveFromFavouriteRecipesUseCase {

    override suspend fun remove(recipe: Recipe): Boolean =
        recipeRepository.removeFavouriteRecipe(recipe) > 0
}