package dev.ranga.recipeexplorer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteRecipeDao {
    @Query("SELECT * FROM $FAVOURITE_RECIPE_TABLE_NAME")
    fun getAllRecipes(): Flow<List<FavouriteRecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipe(recipe: FavouriteRecipeEntity): Long

    @Query("DELETE FROM $FAVOURITE_RECIPE_TABLE_NAME WHERE id = :recipeId")
    suspend fun deleteRecipe(recipeId: Int): Int

    @Query("SELECT * FROM $FAVOURITE_RECIPE_TABLE_NAME WHERE id = :recipeId")
    suspend fun getRecipe(recipeId: Int): FavouriteRecipeEntity?
}
