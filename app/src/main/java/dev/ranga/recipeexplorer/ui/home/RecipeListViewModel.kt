package dev.ranga.recipeexplorer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ranga.recipeexplorer.analytics.Logger
import dev.ranga.recipeexplorer.api.GetAllRecipesUseCase
import dev.ranga.recipeexplorer.api.GetFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.model.Recipe
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val getAllRecipesUseCase: GetAllRecipesUseCase,
    private val getFavouriteRecipesUseCase: GetFavouriteRecipesUseCase,
    private val logger: Logger,
) : ViewModel() {
    private val pageSize = 20
    private val _allRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val allRecipes: StateFlow<List<Recipe>> = _allRecipes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val favouriteRecipes: StateFlow<RecipesState> =
        getFavouriteRecipesUseCase.favourites()
            .map<List<Recipe>, RecipesState> { favouriteRecipes ->
                RecipesState.Success(favouriteRecipes)
            }
            .catch { error ->
                if (error is CancellationException) throw error
                logger.e("RecipeListViewModel", "Error getting favourite recipes", error)
                emit(RecipesState.Error(error.message ?: "Unknown error"))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = RecipesState.Loading
            )
    private val _allRecipesErrorState = MutableSharedFlow<String?>()
    val allRecipesErrorState: SharedFlow<String?> = _allRecipesErrorState

    init {
        loadRecipeData()
    }

    fun refresh() {
        _allRecipes.value = emptyList()
        loadRecipeData()
    }

    fun loadMoreRecipes() {
        loadRecipeData()
    }

    private fun loadRecipeData() {
        if (isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            val from = _allRecipes.value.size
            try {
                onRecipes(
                    getAllRecipesUseCase.allRecipes(
                        from = from,
                        size = pageSize
                    )
                )
            } catch (error: Throwable) {
                onError(error)
            }
            _isLoading.value = false
        }
    }

    private suspend fun onError(error: Throwable) {
        if (error is CancellationException) throw error

        logger.d("RecipeListViewModel", "onError: $error")
        _allRecipesErrorState.emit(error.message)
        onRecipes(emptyList())
    }

    private fun onRecipes(recipes: List<Recipe>) {
        _allRecipes.update { it + recipes }
    }
}