package dev.ranga.recipeexplorer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.ranga.recipeexplorer.ui.details.RecipeDetailScreen
import dev.ranga.recipeexplorer.ui.home.RecipesHome
import kotlinx.serialization.Serializable

@Serializable
object RecipesHomeScreen

@Serializable
data class RecipeDetailsScreen(val recipeId: Long)

fun NavGraphBuilder.recipesHomeScreen(
    onRecipeClick: (recipeId: Long) -> Unit
) {
    composable<RecipesHomeScreen> {
        RecipesHome(onRecipeClick = onRecipeClick)
    }
}

fun NavGraphBuilder.recipeDetailsScreen(
    onBackClick: () -> Unit,
) {
    composable<RecipeDetailsScreen>{
        RecipeDetailScreen(
            onBackClick = onBackClick,
        )
    }
}

@Composable
fun RecipeExplorerNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = RecipesHomeScreen) {
        recipesHomeScreen(
            onRecipeClick = { recipeId ->
                navController.navigate(RecipeDetailsScreen(recipeId))
            }
        )
        recipeDetailsScreen(
            onBackClick = {
                navController.popBackStack()
            },
        )
    }
}
