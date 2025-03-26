package dev.ranga.recipeexplorer.ui.home

import dev.ranga.recipeexplorer.analytics.Logger
import dev.ranga.recipeexplorer.api.GetAllRecipesUseCase
import dev.ranga.recipeexplorer.api.GetFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.model.Recipe
import dev.ranga.recipeexplorer.testFixures.RecipeTestFixtures
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val getAllRecipesUseCase = mockk<GetAllRecipesUseCase>(relaxed = true)
    private val getFavouriteRecipesUseCase = mockk<GetFavouriteRecipesUseCase>(relaxed = true)
    private val logger = mockk<Logger>(relaxed = true)
    private lateinit var suit: RecipeListViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun mockGetAllRecipesUseCase(expectedRecipes: List<List<Recipe>>) {
        var count = 0
        coEvery { getAllRecipesUseCase.allRecipes(any(), any()) } coAnswers {
            expectedRecipes[count++]
        }
    }

    @Test
    fun `getAllRecipes initial state`() = runTest {
        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)

        assertEquals(emptyList<Recipe>(), suit.allRecipes.value)
    }

    @Test
    fun `getAllRecipes success`() = runTest {
        val expectedRecipes = listOf(RecipeTestFixtures.recipeList(2))
        mockGetAllRecipesUseCase(expectedRecipes)

        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)
        advanceUntilIdle()

        val actual = suit.allRecipes.value

        assertEquals(expectedRecipes.flatten(), actual)
    }

    @Test
    fun `getAllRecipes empty list`() = runTest {
        val expectedRecipes = listOf(RecipeTestFixtures.recipeList(0))
        mockGetAllRecipesUseCase(expectedRecipes)

        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)
        advanceUntilIdle()

        val actual = suit.allRecipes.value

        assertEquals(expectedRecipes.flatten(), actual)
    }

    @Test
    fun `getAllRecipes error handling`() = runTest {
        // Test if `getAllRecipes` emits `RecipesState.Success` with an empty list and 
        // `allRecipesErrorState` emits an error message when 
        // `getAllRecipesUseCase.allRecipes()` throws an exception.
        coEvery { getAllRecipesUseCase.allRecipes(any(), any()) } throws Exception("Test error")

        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)
        val actualError = suit.allRecipesErrorState.first()
        advanceUntilIdle()

        val actual = suit.allRecipes.value

        assertEquals(emptyList<Recipe>(), actual)
        assertEquals("Test error", actualError)
    }

    @Test
    fun `getFavouriteRecipes initial state`() = runTest {
        // Verify that `getFavouriteRecipes` initially emits `RecipesState.Loading` 
        // when subscribed to.
        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)

        assert(suit.favouriteRecipes.value is RecipesState.Loading)
    }

    @Test
    fun `getFavouriteRecipes success`() = runTest {
        // Check if `getFavouriteRecipes` emits `RecipesState.Success` with a list of 
        // favourite recipes when `getFavouriteRecipesUseCase.favourites()` returns data 
        // successfully.
        val expectedRecipes = RecipeTestFixtures.recipeList(2)
        coEvery { getFavouriteRecipesUseCase.favourites() } returns flowOf(expectedRecipes)

        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)
        val favouriteRecipes = mutableListOf<RecipesState>()
        val job = backgroundScope.launch {
            suit.favouriteRecipes.collect {
                favouriteRecipes.add(it)
            }
        }
        advanceUntilIdle()

        assertEquals(RecipesState.Success(expectedRecipes), suit.favouriteRecipes.value)
        job.cancel()
    }

    @Test
    fun `getFavouriteRecipes empty list`() = runTest {
        // Verify that `getFavouriteRecipes` emits `RecipesState.Success` with an 
        // empty list when `getFavouriteRecipesUseCase.favourites()` returns an empty list.
        val expectedRecipes = RecipeTestFixtures.recipeList(0)
        coEvery { getFavouriteRecipesUseCase.favourites() } returns flowOf(expectedRecipes)

        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)
        val favouriteRecipes = mutableListOf<RecipesState>()
        val job = backgroundScope.launch {
            suit.favouriteRecipes.collect {
                favouriteRecipes.add(it)
            }
        }
        advanceUntilIdle()

        assertEquals(RecipesState.Success(expectedRecipes), suit.favouriteRecipes.value)
        job.cancel()
    }

    @Test
    fun `getFavouriteRecipes error handling`() = runTest {
        // Test if `getFavouriteRecipes` emits `RecipesState.Error` with an error 
        // message when `getFavouriteRecipesUseCase.favourites()` throws an exception.
        val expectedError = "Test error"
        coEvery { getFavouriteRecipesUseCase.favourites() } returns flow {
            throw Exception(expectedError)
        }

        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)
        val favouriteRecipes = mutableListOf<RecipesState>()
        val job = backgroundScope.launch {
            suit.favouriteRecipes.collect {
                favouriteRecipes.add(it)
            }
        }
        advanceUntilIdle()

        assertEquals(RecipesState.Error(expectedError), suit.favouriteRecipes.value)
        job.cancel()
    }

    @Test
    fun `getAllRecipesErrorState error message`() = runTest {
        // Check that when `getAllRecipesUseCase.allRecipes()` throws an error, that
        // `allRecipesErrorState` emits the correct error message.
        coEvery { getAllRecipesUseCase.allRecipes(any(), any()) } throws Exception("Test error")

        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)
        val actualError = suit.allRecipesErrorState.firstOrNull()
        advanceUntilIdle()

        assertEquals("Test error", actualError)
    }

    @Test
    fun `refresh success`() = runTest {
        // Verify that calling `refresh()` triggers a new call to 
        // `getAllRecipesUseCase.allRecipes()` and updates `allRecipes`.
        val expectedRecipes = listOf(emptyList(), RecipeTestFixtures.recipeList(2))
        mockGetAllRecipesUseCase(expectedRecipes)

        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)
        suit.refresh()
        advanceUntilIdle()

        val actual = suit.allRecipes.value

        assertEquals(expectedRecipes.flatten(), actual)
    }

    @Test
    fun `refresh error`() = runTest {
        // Verify that calling `refresh()` when `getAllRecipesUseCase.allRecipes()` 
        // throws an error updates `allRecipes` and emits an error on 
        // `allRecipesErrorState`.
        var count = 0
        coEvery { getAllRecipesUseCase.allRecipes(any(), any()) } coAnswers {
            when (count++) {
                0 -> emptyList()
                else -> throw Exception("Test error")
            }
        }

        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)
        advanceUntilIdle()
        suit.refresh()

        val actualError = suit.allRecipesErrorState.firstOrNull()
        advanceUntilIdle()

        assertEquals("Test error", actualError)

        val actual = suit.allRecipes.value
        advanceUntilIdle()

        assertEquals(emptyList<Recipe>(), actual)
    }

    @Test
    fun `loadMoreRecipes success multiple loads`() = runTest {
        // Verify that multiple calls to `loadMoreRecipes()` correctly
        // append new recipes to the existing list. It would simulate a few
        // successive calls and check that all the recipes are present in the
        // end result, in the correct order.
        val expectedRecipes = listOf(RecipeTestFixtures.recipeList(2), RecipeTestFixtures.recipeList(3))
        mockGetAllRecipesUseCase(expectedRecipes)

        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)
        advanceUntilIdle()
        suit.loadMoreRecipes()
        advanceUntilIdle()

        val actual = suit.allRecipes.value

        assertEquals(expectedRecipes.flatten(), actual)
    }

    @Test
    fun `loadMoreRecipes when already loading`() = runTest {
        // Test that calling `loadMoreRecipes()` while a load operation
        // is already in progress does not trigger a new data fetch.
        val expectedRecipes = listOf(emptyList(), RecipeTestFixtures.recipeList(2))
        mockGetAllRecipesUseCase(expectedRecipes)

        suit = RecipeListViewModel(getAllRecipesUseCase, getFavouriteRecipesUseCase, logger)
        advanceUntilIdle()
        suit.loadMoreRecipes()
        suit.loadMoreRecipes()
        advanceUntilIdle()

        val actual = suit.allRecipes.value

        assertEquals(expectedRecipes.flatten(), actual)
    }
}