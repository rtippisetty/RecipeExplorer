package dev.ranga.recipeexplorer.domain

import dev.ranga.recipeexplorer.api.GetAllRecipesUseCase
import dev.ranga.recipeexplorer.api.model.Recipe
import javax.inject.Inject

class GetAllRecipesUseCaseImpl @Inject constructor(
    private val recipeRepository: RecipeRepository
): GetAllRecipesUseCase {
    override suspend fun allRecipes(
        from: Int,
        size: Int,
    ): List<Recipe> {
        return recipeRepository.getAllRecipes(
            from = from,
            size = size,
        )
    }
}