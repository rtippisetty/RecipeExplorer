package dev.ranga.recipeexplorer.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import dev.ranga.recipeexplorer.R
import dev.ranga.recipeexplorer.api.model.Recipe
import dev.ranga.recipeexplorer.ui.common.RecipeImage
import dev.ranga.recipeexplorer.ui.theme.RecipeExplorerTheme

@Composable
fun RecipeCard(
    recipe: Recipe,
    onRecipeClick: (recipeId: Int) -> Unit,
    modifier: Modifier = Modifier,

    ) {
    Card(
        modifier = modifier
            .clickable { onRecipeClick(recipe.id) },
        elevation = CardDefaults.cardElevation(RecipeExplorerTheme.dimensions.extraSmall)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            RecipeImage(
                recipe.thumbnailUrl,
                recipe.name,
                modifier = Modifier
                    .fillMaxHeight(fraction = 0.7f)
            )
            Text(
                text = "Ready in : ${recipe.totalTimeMinutes} mins",
                maxLines = 1,
                fontSize = RecipeExplorerTheme.dimensions.fontSizeSmall,
                modifier = Modifier
                    .padding(RecipeExplorerTheme.dimensions.tiny)
                    .align(Alignment.Start)
            )
            Text(
                text = recipe.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = RecipeExplorerTheme.dimensions.fontSizeMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(RecipeExplorerTheme.dimensions.tiny)
                    .wrapContentSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeCardPreview() {
    RecipeCard(
        recipe = Recipe(
            id = 1,
            name = "Chicken Salad and pasta made of this chicken salad and pasta made of this chicken salad and pasta made of this chicken salad and pasta made of this chicken salad and pasta made of this",
            description = "Pizza description",
            thumbnailUrl = "https://www.themealdb.com/images/media/meals/1548772327.jpg",
            totalTimeMinutes = 30,
        ),
        modifier = Modifier
            .width(RecipeExplorerTheme.dimensions.xxxLarge)
            .height(RecipeExplorerTheme.dimensions.xxxxLarge),
        onRecipeClick = {},
    )
}