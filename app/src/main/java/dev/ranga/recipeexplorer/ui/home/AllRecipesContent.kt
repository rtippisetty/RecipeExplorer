package dev.ranga.recipeexplorer.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.ranga.recipeexplorer.R
import dev.ranga.recipeexplorer.api.model.Recipe
import dev.ranga.recipeexplorer.api.model.UserRatings
import dev.ranga.recipeexplorer.ui.common.Heading
import dev.ranga.recipeexplorer.ui.common.ProgressIndicator
import dev.ranga.recipeexplorer.ui.theme.RecipeExplorerTheme

@Composable
internal fun AllRecipesContent(
    allRecipes: List<Recipe>,
    isLoading: Boolean,
    loadMoreRecipes: () -> Unit,
    onRecipeClick: (recipeId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Heading(name = stringResource(id = R.string.all_recipes_title))
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (allRecipes.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.all_recipes_empty),
                    fontSize = RecipeExplorerTheme.dimensions.fontSizeXLarge,
                    modifier = Modifier.padding(RecipeExplorerTheme.dimensions.large)
                )
                if (isLoading) {
                    ProgressIndicator()
                }
            } else {
                val gridState = rememberLazyGridState()

                val isNearEnd by remember {
                    derivedStateOf {
                        val lastVisibleItemIndex =
                            gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                        lastVisibleItemIndex >= allRecipes.size - 6 // Threshold to load more data
                    }
                }

                LaunchedEffect(isNearEnd, allRecipes.size, isLoading) {
                    if (isNearEnd && !isLoading) {
                        loadMoreRecipes()
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(RecipeExplorerTheme.dimensions.extraSmall),
                    state = gridState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = allRecipes,
                        key = { recipe -> recipe.id }
                    ) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onRecipeClick = onRecipeClick,
                            modifier = Modifier
                                .padding(RecipeExplorerTheme.dimensions.extraSmall)
                                .aspectRatio(1f)
                        )
                    }
                    if (isLoading) {
                        item {
                            ProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AllRecipesPreview() {
    RecipeExplorerTheme {
        AllRecipesContent(
            allRecipes = listOf(
                Recipe(
                    id = 1,
                    name = "Chicken Salad",
                    thumbnailUrl = "https://www.themealdb.com/images/media/meals/1548772327.jpg",
                    totalTimeMinutes = 30,
                    userRatings = UserRatings(
                        score = 0.91
                    )
                ),
                Recipe(
                    id = 2,
                    name = "Pizza with Pasta More and More we have to see what happens",
                    thumbnailUrl = "https://www.themealdb.com/images/media/meals/1548772327.jpg",
                    totalTimeMinutes = 45,
                    userRatings = UserRatings(
                        score = 0.91
                    )
                ),
            ),
            isLoading = false,
            loadMoreRecipes = {},
            onRecipeClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}