package dev.ranga.recipeexplorer.domain

import dev.ranga.recipeexplorer.api.GetRecipeDetailsUseCase
import dev.ranga.recipeexplorer.api.model.RecipeDetail
import javax.inject.Inject

class GetRecipeDetailsUseCaseImpl @Inject constructor(
    private val recipeRepository: RecipeRepository
) : GetRecipeDetailsUseCase {
    override suspend fun details(id: Long): RecipeDetail =
        recipeRepository.getRecipeDetail(id)
}