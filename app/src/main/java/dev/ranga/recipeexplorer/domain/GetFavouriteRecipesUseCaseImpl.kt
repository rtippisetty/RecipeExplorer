package dev.ranga.recipeexplorer.domain

import dev.ranga.recipeexplorer.api.GetFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouriteRecipesUseCaseImpl @Inject constructor(
    private val recipeRepository: RecipeRepository
) : GetFavouriteRecipesUseCase {
    override fun favourites(): Flow<List<Recipe>> {
        return recipeRepository.getFavouriteRecipes()
    }
}