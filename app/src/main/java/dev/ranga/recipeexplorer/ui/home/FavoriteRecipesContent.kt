package dev.ranga.recipeexplorer.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.ranga.recipeexplorer.R
import dev.ranga.recipeexplorer.api.model.Recipe
import dev.ranga.recipeexplorer.ui.common.Heading
import dev.ranga.recipeexplorer.ui.common.ProgressIndicator
import dev.ranga.recipeexplorer.ui.theme.RecipeExplorerTheme

@Composable
internal fun FavoriteRecipesContent(
    favouriteRecipes: RecipesState,
    onRecipeClick: (recipeId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (favouriteRecipes) {
        is RecipesState.Success -> FavoriteRecipes(
            modifier = modifier,
            favouriteRecipes = favouriteRecipes.recipes,
            onRecipeClick = onRecipeClick,
        )

        is RecipesState.Error -> Text(
            text = favouriteRecipes.message,
            fontSize = RecipeExplorerTheme.dimensions.fontSizeLarge,
            modifier = Modifier.padding(RecipeExplorerTheme.dimensions.medium)
        )

        RecipesState.Loading -> ProgressIndicator()
    }
}

@Composable
private fun FavoriteRecipes(
    favouriteRecipes: List<Recipe>,
    onRecipeClick: (recipeId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Heading(name = stringResource(id = R.string.favourite_recipes_title))

        if (favouriteRecipes.isEmpty()) {
            Text(
                text = stringResource(id = R.string.favourite_recipes_empty),
                fontSize = RecipeExplorerTheme.dimensions.fontSizeMedium,
                modifier = Modifier.padding(RecipeExplorerTheme.dimensions.small)
            )
        } else {
            LazyRow(
                modifier = Modifier
                    .padding(RecipeExplorerTheme.dimensions.extraSmall)
                    .fillMaxWidth()
            ) {
                items(
                    count = favouriteRecipes.size,
                    key = { index -> favouriteRecipes[index].id }
                ) { index ->
                    RecipeCard(
                        recipe = favouriteRecipes[index],
                        onRecipeClick = onRecipeClick,
                        modifier = Modifier
                            .padding(RecipeExplorerTheme.dimensions.extraSmall)
                            .width(RecipeExplorerTheme.dimensions.xxxLarge)
                            .height(RecipeExplorerTheme.dimensions.dim200)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FavouriteRecipesPreview() {
    RecipeExplorerTheme {
        FavoriteRecipesContent(
            favouriteRecipes = RecipesState.Success(
                listOf(
                    Recipe(
                        id = 1,
                        name = "Chicken Salad",
                        description = "Pizza description",
                        thumbnailUrl = "https://www.themealdb.com/images/media/meals/1548772327.jpg",
                        totalTimeMinutes = 30,
                    ),
                    Recipe(
                        id = 2,
                        name = "Pizza with Pasta",
                        description = "Pizza description",
                        thumbnailUrl = "https://www.themealdb.com/images/media/meals/1548772327.jpg",
                        totalTimeMinutes = 4,
                    )
                )
            ),
            {},
        )
    }
}