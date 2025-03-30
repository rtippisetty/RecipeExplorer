package dev.ranga.recipeexplorer.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ranga.recipeexplorer.R
import dev.ranga.recipeexplorer.ui.common.TopAppBar

@Composable
fun RecipesHome(
    onRecipeClick: (recipeId: Long) -> Unit,
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

