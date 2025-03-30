package dev.ranga.recipeexplorer.domain

import dev.ranga.recipeexplorer.data.mapper.toRecipeDetails
import dev.ranga.recipeexplorer.testFixures.RecipeTestFixtures
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class GetRecipeDetailsUseCaseImplTest {
    private val recipeRepository = mockk<RecipeRepository>()
    private val suit = GetRecipeDetailsUseCaseImpl(recipeRepository)

    @Test
    fun `successful recipe detail retrieval`() = runTest {
        val expectedDetails = RecipeTestFixtures.recipeDetailResponseDto(1).toRecipeDetails()
        coEvery { recipeRepository.getRecipeDetail(any()) } returns expectedDetails

        val actual = suit.details(1)

        assertEquals(expectedDetails, actual)
    }

    @Test
    fun `repository failure handling`() = runTest {
        val expectedException = Exception("Network error")
        coEvery { recipeRepository.getRecipeDetail(any()) } throws expectedException

        assertThrows<Exception> {
            suit.details(1)
        }
    }

    @ParameterizedTest
    @ValueSource(longs = [-1, 0])
    fun `invalid Id handling negative ID`(
        id: Long
    ) = runTest {
        coEvery { recipeRepository.getRecipeDetail(id) } throws IllegalArgumentException()

        assertThrows<IllegalArgumentException> {
            suit.details(id)
        }
    }
}