package dev.ranga.recipeexplorer.data.local

import androidx.room.Entity

const val FAVOURITE_RECIPE_TABLE_NAME = "favourite_recipe_table"

@Entity(tableName = FAVOURITE_RECIPE_TABLE_NAME, primaryKeys = ["id"])
data class FavouriteRecipeEntity(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnailUrl: String,
    val totalTimeMinutes: Int,
)
