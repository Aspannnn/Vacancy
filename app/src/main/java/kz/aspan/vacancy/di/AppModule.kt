package kz.aspan.vacancy.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kz.aspan.vacancy.common.Constants.BASE_URL
import kz.aspan.vacancy.data.remote.BasicAuthInterceptor
import kz.aspan.vacancy.data.remote.VacancyApi
import kz.aspan.vacancy.data.repository.VacancyRepositoryImpl
import kz.aspan.vacancy.domain.repository.VacancyRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideBasicAuthInterceptor() = BasicAuthInterceptor()


    @Singleton
    @Provides
    fun provideRetrofit(
        basicAuthInterceptor: BasicAuthInterceptor,
    ): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(basicAuthInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideVacancy(retrofit: Retrofit): VacancyApi {
        return retrofit.create(VacancyApi::class.java)
    }

    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun provideVacancyRepository(
        @ApplicationContext context: Context,
        api: VacancyApi
    ): VacancyRepository {
        return VacancyRepositoryImpl(context, api)
    }
}