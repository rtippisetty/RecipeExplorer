package dev.ranga.recipeexplorer.domain

import dev.ranga.recipeexplorer.testFixures.RecipeTestFixtures
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class RemoveFromFavouriteRecipesUseCaseImplTest {
    private val recipeRepository = mockk<RecipeRepository>()
    private val suit = RemoveFromFavouriteRecipesUseCaseImpl(recipeRepository)

    @Test
    fun `successful removal from repository`() = runTest {
        val recipe = RecipeTestFixtures.recipe(1)
        coEvery { recipeRepository.removeFavouriteRecipe(recipe) } returns 1

        val actual = suit.remove(recipe)

        assertTrue(actual)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, -1])
    fun `unsuccessful removal from repository`(
        opsResult: Int
    ) = runTest {
        val recipe = RecipeTestFixtures.recipe(1)
        coEvery { recipeRepository.removeFavouriteRecipe(recipe) } returns opsResult

        val actual = suit.remove(recipe)

        assertFalse(actual)
    }

    @Test
    fun `repository exception`() = runTest {
        val exception = Exception("Test exception")
        val recipe = RecipeTestFixtures.recipe(1)
        coEvery { recipeRepository.removeFavouriteRecipe(recipe) } throws exception

        assertThrows<Exception> {
            suit.remove(recipe)
        }
    }
}