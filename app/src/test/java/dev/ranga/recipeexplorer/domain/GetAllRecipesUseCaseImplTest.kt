package dev.ranga.recipeexplorer.domain

import dev.ranga.recipeexplorer.api.model.Recipe
import dev.ranga.recipeexplorer.testFixures.RecipeTestFixtures
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class GetAllRecipesUseCaseImplTest {
    private val recipeRepository = mockk<RecipeRepository>()
    private val suit = GetAllRecipesUseCaseImpl(recipeRepository)

    @Test
    fun `successful retrieval of recipes`() = runTest {
        val expected = RecipeTestFixtures.recipeList(2)
        coEvery { recipeRepository.getAllRecipes(0, 20) } returns expected

        val actual = suit.allRecipes(0, 20)

        assertEquals(expected, actual)
    }

    @Test
    fun `empty list of recipes`() = runTest {
        val expected = emptyList<Recipe>()
        coEvery { recipeRepository.getAllRecipes(0, 20) } returns expected

        val actual = suit.allRecipes(0, 20)

        assertEquals(expected, actual)
    }

    @Test
    fun `repository throws exception`() = runTest {
        val exception = Exception("Test exception")
        coEvery { recipeRepository.getAllRecipes(0, 20) } throws exception

        assertThrows<Exception> {
            suit.allRecipes(0, 20)
        }
    }

    @Test
    fun `verify correct repository method called`() = runTest {
        coEvery { recipeRepository.getAllRecipes(0, 20) } returns emptyList()

        suit.allRecipes(0, 20)

        coVerify { recipeRepository.getAllRecipes(0, 20) }
    }
}