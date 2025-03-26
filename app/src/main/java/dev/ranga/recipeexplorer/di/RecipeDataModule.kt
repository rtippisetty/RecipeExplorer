package dev.ranga.recipeexplorer.di

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ranga.recipeexplorer.R
import dev.ranga.recipeexplorer.data.local.FavouriteRecipeDao
import dev.ranga.recipeexplorer.data.local.FavouriteRecipeDatabase
import dev.ranga.recipeexplorer.data.RecipeRepositoryImpl
import dev.ranga.recipeexplorer.data.network.RecipeService
import dev.ranga.recipeexplorer.domain.RecipeRepository
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton
import dev.ranga.recipeexplorer.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object RecipeDataModule {
    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    fun provideResource(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    @Singleton
    fun provideRecipeRepository(
        recipeService: RecipeService,
        favouriteRecipeDao: FavouriteRecipeDao
    ): RecipeRepository {
        return RecipeRepositoryImpl(recipeService, favouriteRecipeDao)
    }

    @Provides
    @Singleton
    fun provideRecipeService(client: OkHttpClient): RecipeService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(RecipeService.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(RecipeService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(resources: Resources): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(createAuthInterceptor(resources))
            .addInterceptor(createLoggingInterceptor())
            .build()
    }

    private fun createLoggingInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            if(BuildConfig.DEBUG) {
                Log.d("RecipeService", "Request: ${request.url}")
            }
            chain.proceed(request)
        }
    }

    private fun createAuthInterceptor(resources: Resources): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                    RecipeService.API_KEY_PARAM,
                    resources.getString(R.string.api_key)
                )
                .addHeader(
                    RecipeService.API_HOST_PARAM,
                    resources.getString(R.string.api_host)
                )
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    fun provideRecipeDao(database: FavouriteRecipeDatabase): FavouriteRecipeDao {
        return database.favouriteRecipeDao
    }

    @Singleton
    @Provides
    fun provideRecipeDatabase(@ApplicationContext context: Context): FavouriteRecipeDatabase {
        return Room
            .databaseBuilder(
                context = context,
                klass = FavouriteRecipeDatabase::class.java,
                name = "favourite_recipe_database"
            )
            .fallbackToDestructiveMigration(true)
            .build()
    }

}