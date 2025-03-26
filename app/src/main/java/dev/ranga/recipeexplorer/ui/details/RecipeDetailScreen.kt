package dev.ranga.recipeexplorer.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ranga.recipeexplorer.R
import dev.ranga.recipeexplorer.api.model.RecipeDetail
import dev.ranga.recipeexplorer.ui.common.ProgressIndicator
import dev.ranga.recipeexplorer.ui.common.RecipeImage
import dev.ranga.recipeexplorer.ui.common.TopAppBar
import dev.ranga.recipeexplorer.ui.common.toPercent
import dev.ranga.recipeexplorer.ui.theme.RecipeExplorerTheme

@Composable
fun RecipeDetailScreen(
    onBackClick: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel(),
) {
    val recipeDetailsState by viewModel.recipeDetailState.collectAsState()
    val isFavorite by viewModel.isFavourite.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "",
                onBackEnabled = true,
                onBackClick = onBackClick,
                actionsEnabled = true,
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.toggleFavourite()
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = "Favourite"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (recipeDetailsState) {
                RecipeDetailState.Loading -> LoadingContent()
                is RecipeDetailState.Success -> SuccessContent(
                    recipeDetails = (recipeDetailsState as RecipeDetailState.Success).recipeDetail,
                )

                is RecipeDetailState.Error -> ErrorContent(
                    message = (recipeDetailsState as RecipeDetailState.Error).message,
                )
            }
        }
    }
}

@Composable
private fun SuccessContent(
    recipeDetails: RecipeDetail,
    modifier: Modifier = Modifier

) {
    LazyColumn(
        modifier = modifier
            .padding(RecipeExplorerTheme.dimensions.small)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Text(
                    text = recipeDetails.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = RecipeExplorerTheme.dimensions.fontSizeXLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = RecipeExplorerTheme.dimensions.small)
                        .wrapContentSize(Alignment.CenterStart)
                        .weight(1f)
                )
                Text(
                    text = "Rating: " + recipeDetails.userRatings.score.toPercent(),
                    fontSize = RecipeExplorerTheme.dimensions.fontSizeXLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = RecipeExplorerTheme.dimensions.small)
                        .wrapContentSize(Alignment.CenterEnd)
                )
            }
        }
        item {
            Text(
                text = recipeDetails.description,
                overflow = TextOverflow.Ellipsis,
                fontSize = RecipeExplorerTheme.dimensions.fontSizeLarge,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .padding(top = RecipeExplorerTheme.dimensions.small)
                    .wrapContentSize()
            )
        }
        item {
            RecipeImage(
                imageUrl = recipeDetails.thumbnailUrl,
                name = recipeDetails.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(RecipeExplorerTheme.dimensions.xxxxxLarge)
                    .padding(top = RecipeExplorerTheme.dimensions.small)
            )
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.instruction_title),
                    fontSize = RecipeExplorerTheme.dimensions.fontSizeXLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = RecipeExplorerTheme.dimensions.small)
                )
                Text(
                    text = "" + recipeDetails.totalTimeMinutes + " mins",
                    fontSize = RecipeExplorerTheme.dimensions.fontSizeXLarge,
                )
            }
        }
        items(
            recipeDetails.instructions.size,
        ) { index ->
            Text(
                text = "" + (index + 1) + ". " + recipeDetails.instructions[index].displayText,
                fontSize = RecipeExplorerTheme.dimensions.fontSizeLarge,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .padding(top = RecipeExplorerTheme.dimensions.small)
                    .wrapContentSize()
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.error_title),
            fontSize = RecipeExplorerTheme.dimensions.fontSizeXLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = RecipeExplorerTheme.dimensions.small)
        )
        Text(
            text = message ?: "Unknown error",
            fontSize = RecipeExplorerTheme.dimensions.fontSizeMedium,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(top = RecipeExplorerTheme.dimensions.small)
        )
    }
}

@Composable
private fun LoadingContent() {
    ProgressIndicator()
}