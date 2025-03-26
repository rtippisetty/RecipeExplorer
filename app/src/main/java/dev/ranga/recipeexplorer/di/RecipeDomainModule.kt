package dev.ranga.recipeexplorer.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.ranga.recipeexplorer.analytics.Logger
import dev.ranga.recipeexplorer.analytics.LoggerImpl
import dev.ranga.recipeexplorer.api.AddToFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.RemoveFromFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.GetFavouriteRecipesUseCase
import dev.ranga.recipeexplorer.api.GetAllRecipesUseCase
import dev.ranga.recipeexplorer.api.GetRecipeDetailsUseCase
import dev.ranga.recipeexplorer.domain.AddToFavouriteRecipesUseCaseImpl
import dev.ranga.recipeexplorer.domain.RemoveFromFavouriteRecipesUseCaseImpl
import dev.ranga.recipeexplorer.domain.GetFavouriteRecipesUseCaseImpl
import dev.ranga.recipeexplorer.domain.GetAllRecipesUseCaseImpl
import dev.ranga.recipeexplorer.domain.GetRecipeDetailsUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface RecipeDomainModule {
    @Binds
    fun bindGetRecipesUseCase(impl: GetAllRecipesUseCaseImpl): GetAllRecipesUseCase
    @Binds
    fun bindGetFavouriteRecipesUseCase(impl: GetFavouriteRecipesUseCaseImpl): GetFavouriteRecipesUseCase
    @Binds
    fun bindAddToFavouriteRecipesUseCase(impl: AddToFavouriteRecipesUseCaseImpl): AddToFavouriteRecipesUseCase
    @Binds
    fun bindDeleteFromFavouriteRecipesUseCase(impl: RemoveFromFavouriteRecipesUseCaseImpl): RemoveFromFavouriteRecipesUseCase
    @Binds
    fun bindGetRecipeDetailsUseCase(impl: GetRecipeDetailsUseCaseImpl): GetRecipeDetailsUseCase
    @Binds
    fun bindLogger(impl: LoggerImpl): Logger

}