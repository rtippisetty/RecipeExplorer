package dev.ranga.recipeexplorer.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.ranga.recipeexplorer.ui.theme.RecipeExplorerTheme


@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier.fillMaxSize()
) {
    Box(
        modifier = modifier
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(RecipeExplorerTheme.dimensions.xxLarge),
            color = RecipeExplorerTheme.colorScheme.secondary,
            trackColor = RecipeExplorerTheme.colorScheme.surfaceVariant,
        )
    }
}

@Preview
@Composable
private fun ProgressIndicatorPreview() {
    RecipeExplorerTheme {
        ProgressIndicator()
    }
}