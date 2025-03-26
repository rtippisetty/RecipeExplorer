package dev.ranga.recipeexplorer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.ranga.recipeexplorer.ui.navigation.RecipeExplorerNavHost
import dev.ranga.recipeexplorer.ui.theme.RecipeExplorerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            RecipeExplorerTheme {
                RecipeExplorerNavHost(navController)
            }
        }
    }
}
