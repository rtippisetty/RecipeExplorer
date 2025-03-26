package dev.ranga.recipeexplorer.domain

import dev.ranga.recipeexplorer.testFixures.RecipeTestFixtures
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows

internal class AddToFavouriteRecipesUseCaseImplTest {

    private val recipeRepository = mockk<RecipeRepository>()
    private val suit = AddToFavouriteRecipesUseCaseImpl(recipeRepository)

    @Test
    fun `successful recipe addition`() = runTest {
        val recipe = RecipeTestFixtures.recipe(1)
        coEvery { recipeRepository.addFavouriteRecipe(recipe) } returns 1

        val actual = suit.add(recipe)

        assertTrue(actual)
    }

    @Test
    fun `unsuccessful recipe addition`() = runTest {
        val recipe = RecipeTestFixtures.recipe(1)
        coEvery { recipeRepository.addFavouriteRecipe(recipe) } returns -1L

        val actual = suit.add(recipe)

        assertFalse(actual)
    }

    @Test
    fun `repository exception handling`() = runTest {
        val exception = Exception("Test exception")
        coEvery { recipeRepository.addFavouriteRecipe(any()) } throws exception

        assertThrows<Exception> {
            suit.add(RecipeTestFixtures.recipe(1))
        }
    }
}