package dev.ranga.recipeexplorer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavouriteRecipeEntity::class], version = 1, exportSchema = false)
abstract class FavouriteRecipeDatabase : RoomDatabase() {
    abstract val favouriteRecipeDao: FavouriteRecipeDao
}