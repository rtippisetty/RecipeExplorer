package dev.ranga.recipeexplorer.data

import android.database.sqlite.SQLiteException
import dev.ranga.recipeexplorer.api.model.Recipe
import dev.ranga.recipeexplorer.data.local.FavouriteRecipeDao
import dev.ranga.recipeexplorer.data.mapper.toRecipe
import dev.ranga.recipeexplorer.data.mapper.toRecipeDetails
import dev.ranga.recipeexplorer.data.network.RecipeService
import dev.ranga.recipeexplorer.testFixures.RecipeTestFixtures
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.IOException

internal class RecipeRepositoryImplTest {
    private val recipeService = mockk<RecipeService>()
    private val favouriteRecipeDao = mockk<FavouriteRecipeDao>()
    private val suit = RecipeRepositoryImpl(recipeService, favouriteRecipeDao)

    @Test
    fun `getAllRecipes success`() = runTest {
        val fakeResponse = RecipeTestFixtures.recipeListResponseDto(4)
        coEvery { recipeService.getRecipes() } returns fakeResponse
        val expected = fakeResponse.results.map { it.toRecipe() }

        val actual = suit.getAllRecipes(0, 20)

        assertEquals(expected, actual)
    }

    @Test
    fun `getAllRecipes returns an empty list when the API returns an empty result set`() = runTest {
        val fakeResponse = RecipeTestFixtures.recipeListResponseDto(0)
        coEvery { recipeService.getRecipes() } returns fakeResponse
        val expected = emptyList<Recipe>()

        val actual = suit.getAllRecipes(0, 20)

        assertEquals(expected, actual)
    }

    @Test
    fun `getAllRecipes network failure`() = runTest {
        val expectedException = IOException("Network error")
        coEvery { recipeService.getRecipes() } throws expectedException

        val actualException = assertThrows<IOException> {
            suit.getAllRecipes(0, 20)
        }

        assertEquals(expectedException.message, actualException.message)
    }

    @Test
    fun `getAllRecipes correctly maps the parse error to null recipe`() = runTest {
        val fakeResponse = RecipeTestFixtures.recipeListResponseDto(
            count = 1,
            results = RecipeTestFixtures.recipeDtoList(1)[0].copy(
                thumbnailUrl = "invalid-url"
            ).let { listOf(it) }
        )
        coEvery { recipeService.getRecipes() } returns fakeResponse
        val expected = emptyList<Recipe>()

        val actual = suit.getAllRecipes(0, 20)

        assertEquals(expected, actual)
    }

    @Test
    fun `getRecipeDetail success`() = runTest {
        val fakeResponse = RecipeTestFixtures.recipeDetailResponseDto(1)
        coEvery { recipeService.getRecipeDetails(fakeResponse.id) } returns fakeResponse
        val expected = fakeResponse.toRecipeDetails()

        val actual = suit.getRecipeDetail(fakeResponse.id)

        assertEquals(expected, actual)
    }

    @Test
    fun `getRecipeDetail invalid recipeId throws exception`() = runTest {
        val expectedException = IllegalArgumentException("Invalid recipeId")
        coEvery { recipeService.getRecipeDetails(any()) } throws expectedException

        assertThrows<IllegalArgumentException> {
            suit.getRecipeDetail(0)
        }
    }

    @ParameterizedTest
    @ValueSource(longs = [-1L, 0L])
    fun `getRecipeDetail handling invalid Id`(
        id: Long,
    ) = runTest {
        assertThrows<IllegalArgumentException> {
            suit.getRecipeDetail(id)
        }
    }

    @Test
    fun `getRecipeDetail network failure throws exception`() = runTest {
        val expectedException = IOException("Network error")
        coEvery { recipeService.getRecipeDetails(any()) } throws expectedException

        assertThrows<IOException> {
            suit.getRecipeDetail(1)
        }
    }

    @Test
    fun `getRecipeDetail mapping error throws exception`() = runTest {
        val fakeResponse = RecipeTestFixtures.recipeDetailResponseDto(1).copy(
            thumbnailUrl = "invalid-url"
        )
        coEvery { recipeService.getRecipeDetails(fakeResponse.id) } returns fakeResponse

        assertThrows<IllegalStateException> {
            suit.getRecipeDetail(fakeResponse.id)
        }
    }

    @Test
    fun `getFavouriteRecipes empty db`() = runTest {
        val expected = emptyList<Recipe>()
        coEvery { favouriteRecipeDao.getAllRecipes() } returns emptyFlow()

        val actual = suit.getFavouriteRecipes().toList()

        assertEquals(expected, actual)
    }

    @Test
    fun `getFavouriteRecipes multiple recipes`() = runTest {
        val fakeEntities = RecipeTestFixtures.favouriteRecipeEntities(3)
        val expected = fakeEntities.map { it.toRecipe() }
        coEvery { favouriteRecipeDao.getAllRecipes() } returns listOf(fakeEntities).asFlow()

        val actual = suit.getFavouriteRecipes().first()

        assertEquals(expected, actual)
    }

    @Test
    fun `getFavouriteRecipes db access error`() = runTest {
        val expectedException = SQLiteException("Database error")
        coEvery { favouriteRecipeDao.getAllRecipes() } throws expectedException

        assertThrows<SQLiteException> {
            suit.getFavouriteRecipes().first()
        }
    }

    @Test
    fun `getFavouriteRecipes maps parsing error correctly`() = runTest {
        val fakeEntities = listOf(
            RecipeTestFixtures.favouriteRecipeEntities(1)[0].copy(
                thumbnailUrl = "invalid-url"
            )
        )
        coEvery { favouriteRecipeDao.getAllRecipes() } returns listOf(fakeEntities).asFlow()
        val expected = emptyList<Recipe>()

        val actual = suit.getFavouriteRecipes().first()

        assertEquals(expected, actual)
    }

    @Test
    fun `addFavouriteRecipe success`() = runTest {
        val fakeRecipeEntity = RecipeTestFixtures.favouriteRecipeEntities(1)[0]
        coEvery { favouriteRecipeDao.insertRecipe(fakeRecipeEntity) } returns 1

        val actual = suit.addFavouriteRecipe(fakeRecipeEntity.toRecipe())

        assertEquals(1, actual)
    }

    @Test
    fun `addFavouriteRecipe ignores duplicate recipe`() = runTest {
        val fakeRecipeEntity = RecipeTestFixtures.favouriteRecipeEntities(1)[0]
        coEvery { favouriteRecipeDao.insertRecipe(fakeRecipeEntity) } returns 0

        val actual = suit.addFavouriteRecipe(fakeRecipeEntity.toRecipe())

        assertEquals(0, actual)
    }

    @Test
    fun `addFavouriteRecipe db access error`() = runTest {
        val expectedException = SQLiteException("Database error")
        coEvery { favouriteRecipeDao.insertRecipe(any()) } throws expectedException

        assertThrows<SQLiteException> {
            suit.addFavouriteRecipe(RecipeTestFixtures.favouriteRecipeEntities(1)[0].toRecipe())
        }
    }

    @Test
    fun `removeFavouriteRecipe success`() = runTest {
        val fakeRecipeEntity = RecipeTestFixtures.favouriteRecipeEntities(1)[0]
        coEvery { favouriteRecipeDao.deleteRecipe(fakeRecipeEntity.id) } returns 1

        val actual = suit.removeFavouriteRecipe(fakeRecipeEntity.toRecipe())

        assertEquals(1, actual)
    }

    @Test
    fun `removeFavouriteRecipe non existent recipe`() = runTest {
        val fakeRecipeEntity = RecipeTestFixtures.favouriteRecipeEntities(1)[0]
        coEvery { favouriteRecipeDao.deleteRecipe(fakeRecipeEntity.id) } returns 0

        val actual = suit.removeFavouriteRecipe(fakeRecipeEntity.toRecipe())

        assertEquals(0, actual)
    }

    @Test
    fun `removeFavouriteRecipe db access error`() = runTest {
        val expectedException = SQLiteException("Database error")
        coEvery { favouriteRecipeDao.deleteRecipe(any()) } throws expectedException

        assertThrows<SQLiteException> {
            suit.removeFavouriteRecipe(RecipeTestFixtures.favouriteRecipeEntities(1)[0].toRecipe())
        }
    }
}