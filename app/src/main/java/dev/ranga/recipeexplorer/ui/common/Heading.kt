package dev.ranga.recipeexplorer.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import dev.ranga.recipeexplorer.ui.theme.RecipeExplorerTheme

@Composable
internal fun Heading(name: String) {
    Text(
        text = name,
        fontSize = RecipeExplorerTheme.dimensions.fontSizeXLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(RecipeExplorerTheme.dimensions.medium)
    )
}