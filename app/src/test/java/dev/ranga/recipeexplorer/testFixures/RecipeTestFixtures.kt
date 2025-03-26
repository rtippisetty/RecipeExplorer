package dev.ranga.recipeexplorer.testFixures

import dev.ranga.recipeexplorer.api.model.Recipe
import dev.ranga.recipeexplorer.api.model.UserRatings
import dev.ranga.recipeexplorer.data.local.FavouriteRecipeEntity
import dev.ranga.recipeexplorer.data.mapper.toRecipe
import dev.ranga.recipeexplorer.data.mapper.toRecipeDetails
import dev.ranga.recipeexplorer.data.network.InstructionDto
import dev.ranga.recipeexplorer.data.network.RecipeDto
import dev.ranga.recipeexplorer.data.network.RecipeListResponseDto
import dev.ranga.recipeexplorer.data.network.RecipeMoreInfoDto
import dev.ranga.recipeexplorer.data.network.UserRatingsDto

object RecipeTestFixtures {
    private const val URL ="https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"

    fun recipeListResponseDto(
        count: Int = 0,
        results: List<RecipeDto> = recipeDtoList(count)
    ) = RecipeListResponseDto(
        count = count,
        results = results
    )

    fun recipeDtoList(
        count: Int = 1,
    ) = List(count) { index ->
        val id = index + 1
        RecipeDto(
            id = id,
            name = "Recipe $id",
            description = "Recipe $id description",
            totalTimeMinutes = id * 10,
            thumbnailUrl = URL
        )
    }

    fun recipeDetailResponseDto(id: Int) = RecipeMoreInfoDto(
        id = id,
        name = "Recipe $id",
        description = "Recipe $id description",
        instructions = listOf(
            InstructionDto(
                id = 1,
                stepNumber = 1,
                displayText = "Step 1",
            ),
            InstructionDto(
                id = 2,
                stepNumber = 2,
                displayText = "Step 2",
            )
        ),
        thumbnailUrl = URL,
        cookTimeMinutes = 10,
        prepTimeMinutes = 5,
        totalTimeMinutes = 15,
        userRatings = UserRatingsDto(
            score = 4.5
        )
    )

    fun favouriteRecipeEntities(
        count: Int = 1,
    ): List<FavouriteRecipeEntity> = List(count) { index ->
        val id = index + 1
        FavouriteRecipeEntity(
            id = id,
            name = "Recipe $id",
            description = "Recipe $id description",
            totalTimeMinutes = id * 10,
            thumbnailUrl = URL,
            userRatings = 0.91
        )
    }

    fun recipe(id: Int) = Recipe(
        id = id,
        name = "Recipe $id",
        description = "Recipe $id description",
        totalTimeMinutes = id * 10,
        thumbnailUrl = URL,
        userRatings = UserRatings(0.89)
    )

    fun recipeList(
        count: Int = 1,
        recipeDtoList: List<RecipeDto> = recipeDtoList(count)
    ) = recipeDtoList.map {
        it.toRecipe()
    }
    fun recipeDetail(id: Int) = recipeDetailResponseDto(id).toRecipeDetails()
}