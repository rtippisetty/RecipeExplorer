package dev.ranga.recipeexplorer.ui.home

import androidx.activity.result.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ranga.recipeexplorer.R
import dev.ranga.recipeexplorer.api.model.Recipe
import dev.ranga.recipeexplorer.ui.common.ProgressIndicator
import dev.ranga.recipeexplorer.ui.common.TopAppBar
import dev.ranga.recipeexplorer.ui.theme.RecipeExplorerTheme

@Composable
fun RecipesHome(
    onRecipeClick: (recipeId: Int) -> Unit,
    viewModel: RecipeListViewModel = hiltViewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarMessage by viewModel.allRecipesErrorState.collectAsState(initial = null)
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.home_title),
                actionsEnabled = true,
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Outlined.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { padding ->
        LaunchedEffect(snackBarMessage) {
            snackBarMessage?.let { message ->
                snackBarHostState.showSnackbar(message = message)
            }
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            FavoriteRecipesContent(
                favouriteRecipes = viewModel.favouriteRecipes.collectAsState().value,
                onRecipeClick = onRecipeClick
            )
            AllRecipesContent(
                allRecipes = viewModel.allRecipes.collectAsState().value,
                isLoading = viewModel.isLoading.collectAsState().value,
                loadMoreRecipes = viewModel::loadMoreRecipes,
                onRecipeClick = onRecipeClick,
            )
        }
    }
}

@Composable
private fun FavoriteRecipesContent(
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
        Text(
            text = stringResource(id = R.string.favourite_recipes_title),
            fontSize = RecipeExplorerTheme.dimensions.fontSizeXLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(RecipeExplorerTheme.dimensions.small)
        )
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
                            .height(RecipeExplorerTheme.dimensions.xxxxLarge)
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