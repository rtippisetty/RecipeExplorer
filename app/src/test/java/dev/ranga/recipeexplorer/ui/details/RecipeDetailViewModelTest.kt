package dev.ranga.recipeexplorer.ui.details

import androidx.lifecycle.SavedStateHandle
import dev.ranga.recipeexplorer.analytics.Logger
import dev.ranga.recipeexplorer.api.AddToFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.GetFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.GetRecipeDetailsUseCase
import dev.ranga.recipeexplorer.api.RemoveFromFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.model.toRecipe
import dev.ranga.recipeexplorer.testFixures.RecipeTestFixtures
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
internal class RecipeDetailViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private val getRecipeDetailsUseCase = mockk<GetRecipeDetailsUseCase>(relaxed = true)
    private val getFavouriteRecipesUseCase = mockk<GetFavouriteRecipesUseCase>(relaxed = true)
    private val addToFavouriteRecipesUseCase = mockk<AddToFavouriteRecipesUseCase>(relaxed = true)
    private val removeFromFavouriteRecipesUseCase =
        mockk<RemoveFromFavouriteRecipesUseCase>(relaxed = true)
    private val logger = mockk<Logger>(relaxed = true)

    private lateinit var suit: RecipeDetailViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        suit = RecipeDetailViewModel(
            savedStateHandle,
            getFavouriteRecipesUseCase,
            getRecipeDetailsUseCase,
            addToFavouriteRecipesUseCase,
            removeFromFavouriteRecipesUseCase,
            logger
        )
    }

    private fun mockRecipeId(recipeId: Int) {
        every { savedStateHandle.get<Int>("recipeId") } returns recipeId
    }

    @Test
    fun `getRecipeDetailState initial state`() = runTest {
        mockRecipeId(1)
        createViewModel()
        assertEquals(RecipeDetailState.Loading, suit.recipeDetailState.value)
    }

    @Test
    fun `getRecipeDetailState success`() = runTest {
        val expectedRecipeDetail = RecipeTestFixtures.recipeDetail(1)
        mockRecipeId(1)
        coEvery { getRecipeDetailsUseCase.details(any()) } returns expectedRecipeDetail

        createViewModel()
        advanceUntilIdle()

        assertEquals(RecipeDetailState.Success(expectedRecipeDetail), suit.recipeDetailState.value)
    }

    @Test
    fun `getRecipeDetailState error`() = runTest {
        val expectedException = Exception("Network error")
        coEvery { getRecipeDetailsUseCase.details(any()) } throws expectedException
        mockRecipeId(1)
        createViewModel()
        advanceUntilIdle()

        assertEquals(RecipeDetailState.Error("Network error"), suit.recipeDetailState.value)
    }

    @Test
    fun `getRecipeDetailState stays Loading with invalid id`() {
        mockRecipeId(-1)
        createViewModel()

        assertEquals(RecipeDetailState.Loading, suit.recipeDetailState.value)
    }

    @Test
    fun `isFavourite initial state`() {
        mockRecipeId(1)

        createViewModel()

        assertEquals(false, suit.isFavourite.value)
    }

    @Test
    fun `isFavourite when recipe is favourite`() = runTest {
        mockRecipeId(1)
        val expectedRecipeDetail = RecipeTestFixtures.recipeDetail(1)
        val favouriteRecipe = expectedRecipeDetail.toRecipe()
        coEvery { getFavouriteRecipesUseCase.favourites() } returns flowOf(listOf(favouriteRecipe))
        coEvery { getRecipeDetailsUseCase.details(any()) } returns expectedRecipeDetail

        createViewModel()

        val isFavouriteValues = mutableListOf<Boolean>()
        val job = backgroundScope.launch {
            suit.isFavourite.collect {
                isFavouriteValues.add(it)
            }
        }
        advanceUntilIdle()

        val actual = suit.isFavourite.value

        assertEquals(true, actual)
        job.cancel()
    }

    @Test
    fun `isFavourite when recipe is not favourite`() = runTest {
        mockRecipeId(1)
        val expectedRecipeDetail = RecipeTestFixtures.recipeDetail(1)
        val favouriteRecipe = RecipeTestFixtures.recipeDetail(2).toRecipe()
        coEvery { getFavouriteRecipesUseCase.favourites() } returns flowOf(listOf(favouriteRecipe))
        coEvery { getRecipeDetailsUseCase.details(any()) } returns expectedRecipeDetail

        createViewModel()
        val isFavouriteValues = mutableListOf<Boolean>()
        val job = backgroundScope.launch {
            suit.isFavourite.collect {
                isFavouriteValues.add(it)
            }
        }
        assertEquals(false, suit.isFavourite.value)

        advanceUntilIdle()

        val actual = suit.isFavourite.value

        assertEquals(false, actual)
        job.cancel()
    }

    @Test
    fun `isFavourite error then catches the exception`() = runTest {
        mockRecipeId(1)
        val expectedException = Exception("Network error")
        coEvery { getFavouriteRecipesUseCase.favourites() } returns flow {
            expectedException
        }
        createViewModel()
        val isFavouriteValues = mutableListOf<Boolean>()
        val job = backgroundScope.launch {
            suit.isFavourite.collect {
                isFavouriteValues.add(it)
            }
        }
        advanceUntilIdle()

        val actual = suit.isFavourite.value

        assertEquals(false, actual)
        job.cancel()
    }

    @Test
    fun `toggleFavourite add to favourite`() = runTest {
        mockRecipeId(1)
        val expectedRecipeDetail = RecipeTestFixtures.recipeDetail(1)
        val favouriteRecipesFlow = MutableStateFlow(
            listOf(RecipeTestFixtures.recipeDetail(2).toRecipe())
        )
        coEvery { getRecipeDetailsUseCase.details(any()) } returns expectedRecipeDetail
        coEvery { getFavouriteRecipesUseCase.favourites() } returns favouriteRecipesFlow

        createViewModel()
        advanceUntilIdle()

        suit.toggleFavourite()
        advanceUntilIdle()

        coVerify { addToFavouriteRecipesUseCase.add(any()) }
    }

    @Test
    fun `toggleFavourite remove from favourite`() = runTest {
        mockRecipeId(1)
        val expectedRecipeDetail = RecipeTestFixtures.recipeDetail(1)
        val favouriteRecipesFlow = MutableStateFlow(
            listOf(RecipeTestFixtures.recipeDetail(1).toRecipe())
        )
        coEvery { getRecipeDetailsUseCase.details(any()) } returns expectedRecipeDetail
        coEvery { getFavouriteRecipesUseCase.favourites() } returns favouriteRecipesFlow

        createViewModel()
        val isFavouriteValues = mutableListOf<Boolean>()
        val job = backgroundScope.launch {
            suit.isFavourite.collect {
                isFavouriteValues.add(it)
            }
        }
        advanceUntilIdle()
        assertEquals(true, suit.isFavourite.value)

        suit.toggleFavourite()
        advanceUntilIdle()

        coVerify { removeFromFavouriteRecipesUseCase.remove(any()) }
        job.cancel()
    }
}