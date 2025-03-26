package dev.ranga.recipeexplorer.data

import dev.ranga.recipeexplorer.api.model.Recipe
import dev.ranga.recipeexplorer.api.model.RecipeDetail
import dev.ranga.recipeexplorer.data.local.FavouriteRecipeDao
import dev.ranga.recipeexplorer.data.mapper.toFavouriteRecipeEntity
import dev.ranga.recipeexplorer.data.mapper.toRecipe
import dev.ranga.recipeexplorer.data.mapper.toRecipeDetails
import dev.ranga.recipeexplorer.data.network.RecipeService
import dev.ranga.recipeexplorer.domain.RecipeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class RecipeRepositoryImpl @Inject constructor(
    private val recipeService: RecipeService,
    private val favouriteRecipeDao: FavouriteRecipeDao,
) : RecipeRepository {
    override suspend fun getAllRecipes(from: Int, size: Int): List<Recipe> = supervisorScope {
        val response = recipeService.getRecipes(from, size)
        response.results.map { recipeDto ->
            async {
                try {
                    recipeDto.toRecipe()
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    null
                }
            }
        }.awaitAll().filterNotNull()
    }

    override suspend fun getRecipeDetail(recipeId: Int): RecipeDetail {
        require(recipeId > 0) { "Invalid recipe ID" }

        return recipeService.getRecipeDetails(recipeId).toRecipeDetails()
    }


    override fun getFavouriteRecipes(): Flow<List<Recipe>> {
        return favouriteRecipeDao.getAllRecipes().map { recipeEntities ->
            recipeEntities.mapNotNull { favouriteRecipeEntity ->
                try {
                    favouriteRecipeEntity.toRecipe()
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    null
                }
            }
        }
    }

    override suspend fun addFavouriteRecipe(recipe: Recipe): Long =
        favouriteRecipeDao.insertRecipe(recipe.toFavouriteRecipeEntity())

    override suspend fun removeFavouriteRecipe(recipe: Recipe): Int =
        favouriteRecipeDao.deleteRecipe(recipe.id)
}
