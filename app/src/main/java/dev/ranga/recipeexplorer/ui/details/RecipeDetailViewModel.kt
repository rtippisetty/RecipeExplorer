package dev.ranga.recipeexplorer.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ranga.recipeexplorer.analytics.Logger
import dev.ranga.recipeexplorer.api.AddToFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.GetFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.GetRecipeDetailsUseCase
import dev.ranga.recipeexplorer.api.RemoveFromFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.model.RecipeDetail
import dev.ranga.recipeexplorer.api.model.toRecipe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getFavouriteRecipesUseCase: GetFavouriteRecipesUseCase,
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val addToFavouriteRecipesUseCase: AddToFavouriteRecipesUseCase,
    private val removeFromFavouriteRecipesUseCase: RemoveFromFavouriteRecipesUseCase,
    private val logger: Logger,
) : ViewModel() {
    private val recipeId: Int = savedStateHandle["recipeId"] ?: 0

    private val favouriteToggleEvents = MutableSharedFlow<Unit>()

    private val _recipeDetailState = MutableStateFlow<RecipeDetailState>(RecipeDetailState.Loading)
    val recipeDetailState: StateFlow<RecipeDetailState> = _recipeDetailState

    val isFavourite: StateFlow<Boolean> = getFavouriteRecipesUseCase.favourites()
        .map { favouriteRecipes ->
            favouriteRecipes.any { it.id == recipeId }
        }.catch {
            logger.e("RecipeDetailViewModel", "Error checking favourite", it)
            emit(false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    init {
        if (recipeId != 0) {
            loadRecipeDetails(recipeId)
        }
        observeFavouriteToggleEvents()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeFavouriteToggleEvents() {
        viewModelScope.launch {
            favouriteToggleEvents.flatMapLatest {
                val isFavourite = isFavourite.value
                val recipe = (recipeDetailState.value as? RecipeDetailState.Success)
                    ?.recipeDetail
                    ?.toRecipe()
                flow<Boolean> {
                    if (recipe == null) {
                        emit(false)
                        return@flow
                    }
                    if (isFavourite) {
                        emit(removeFromFavouriteRecipesUseCase.remove(recipe))
                    } else {
                        emit(addToFavouriteRecipesUseCase.add(recipe))
                    }
                }
            }.catch {
                logger.e("RecipeDetailViewModel", "Error toggling favourite", it)
            }.collect { success ->
                if (success) {
                    logger.d("RecipeDetailViewModel", "Favourite toggled successfully")
                } else {
                    logger.d("RecipeDetailViewModel", "Favourite toggle failed")
                }
            }
        }
    }

    fun toggleFavourite() {
        viewModelScope.launch {
            favouriteToggleEvents.emit(Unit)
        }
    }

    private fun loadRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            try {
                onFetchRecipeDetailsSuccess(getRecipeDetailsUseCase.details(recipeId))
            } catch (error: Throwable) {
                onFetchRecipeDetailError(error)
            }
        }
    }

    private fun onFetchRecipeDetailError(error: Throwable) {
        _recipeDetailState.update { RecipeDetailState.Error(error.message) }
    }

    private fun onFetchRecipeDetailsSuccess(recipeDetails: RecipeDetail) {
        _recipeDetailState.update { RecipeDetailState.Success(recipeDetails) }
    }
}