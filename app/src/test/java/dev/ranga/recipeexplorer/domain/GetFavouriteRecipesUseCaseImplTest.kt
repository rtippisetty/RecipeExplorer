package dev.ranga.recipeexplorer.domain

import dev.ranga.recipeexplorer.api.model.Recipe
import dev.ranga.recipeexplorer.testFixures.RecipeTestFixtures
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows

internal class GetFavouriteRecipesUseCaseImplTest {
    private val recipeRepository = mockk<RecipeRepository>()
    private val suit = GetFavouriteRecipesUseCaseImpl(recipeRepository)

    @Test
    fun `successful retrieval of favourite recipes`() = runTest {
        val expected = RecipeTestFixtures.recipeList(2)
        coEvery { recipeRepository.getFavouriteRecipes() } returns flowOf(expected)

        val actual = suit.favourites().first()

        assertEquals(expected, actual)
    }

    @Test
    fun `empty list of favourite recipes`() = runTest {
        val expected = emptyList<Recipe>()
        coEvery { recipeRepository.getFavouriteRecipes() } returns flowOf(expected)

        val actual = suit.favourites().first()

        assertEquals(expected, actual)
    }

    @Test
    fun `repository error handling`() = runTest {
        val exception = Exception("Test exception")
        coEvery { recipeRepository.getFavouriteRecipes() } throws exception

        assertThrows<Exception> {
            suit.favourites().first()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `multiple emissions of recipes separated in time are collected`() = runTest {
        val fakeFlow: Flow<List<Recipe>> = flowOf(
            RecipeTestFixtures.recipeList(1),
            RecipeTestFixtures.recipeList(2),
            RecipeTestFixtures.recipeList(3)
        ).onEach { delay(1000) }
        coEvery { recipeRepository.getFavouriteRecipes() } returns fakeFlow
        val expected = listOf (
            1000L to RecipeTestFixtures.recipeList(1),
            2000L to RecipeTestFixtures.recipeList(2),
            3000L to RecipeTestFixtures.recipeList(3)
        ).toList()

        var actual = suit.favourites()
            .map { currentTime to it }
            .toList()

        assertEquals(expected, actual)
    }
}