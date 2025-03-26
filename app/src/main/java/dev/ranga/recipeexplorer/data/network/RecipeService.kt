package dev.ranga.recipeexplorer.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeService {
    @GET("recipes/list")
    suspend fun getRecipes(
        @Query("from") from: Int = FROM_INDEX,
        @Query("size") size: Int = SIZE
    ): RecipeListResponseDto

    @GET("recipes/get-more-info")
    suspend fun getRecipeDetails(
        @Query("id") id: Int
    ): RecipeMoreInfoDto

    companion object {
        private const val FROM_INDEX: Int = 0
        private const val SIZE: Int = 20

        const val BASE_URL = "https://tasty.p.rapidapi.com/"
        const val API_KEY_PARAM = "x-rapidapi-key"
        const val API_HOST_PARAM = "x-rapidapi-host"
    }
}