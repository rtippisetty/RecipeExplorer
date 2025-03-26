package dev.ranga.recipeexplorer.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.ranga.recipeexplorer.R
import dev.ranga.recipeexplorer.ui.theme.RecipeExplorerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    onBackEnabled: Boolean = false,
    onBackClick: () -> Unit = {},
    actionsEnabled: Boolean = false,
    actions: @Composable () -> Unit = {},
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        title = {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
        },
        navigationIcon = {
            if (onBackEnabled) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors()
            .copy(containerColor = RecipeExplorerTheme.colorScheme.primaryContainer),
        actions = {
            if (actionsEnabled) {
                actions()
            }
        }
    )
}

@Preview
@Composable
private fun TodoTopAppBarPreview() {
    TopAppBar(title = "Recipes")
}

@Preview
@Composable
private fun TodoTopAppBarWithBackPreview() {
    TopAppBar(title = "Recipe", onBackEnabled = true)
}

@Preview
@Composable
private fun TodoTopAppBarWithActionsPreview() {
    TopAppBar(title = "Recipe", actionsEnabled = true) {
        IconButton(onClick = {}) {
            Icon(Icons.Outlined.FavoriteBorder, contentDescription = "save")
        }
    }
}

@Preview
@Composable
private fun TodoTopAppBarWithActionsFavPreview() {
    TopAppBar(title = "Recipe", actionsEnabled = true) {
        IconButton(onClick = {}) {
            Icon(Icons.Filled.Favorite, contentDescription = "save")
        }
    }
}