package com.example.newmarvelcompose.di

import com.example.newmarvelcompose.BuildConfig
import com.example.newmarvelcompose.data.remote.MarvelApi
import com.example.newmarvelcompose.domain.MarvelRepositoryImpl
import com.example.newmarvelcompose.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    private val httpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(
            Interceptor { chain ->
                val originalRequest = chain.request()
                val originalHttpUrl = originalRequest.url

                // https://developer.marvel.com/documentation/authorization
                val timeStamp = System.currentTimeMillis()
                val input = "${timeStamp}${BuildConfig.MARVEL_PRIVATE_KEY}${BuildConfig.MARVEL_PUBLIC_KEY}"
                val hash = MessageDigest.getInstance("MD5")
                    .digest(input.toByteArray()).joinToString("") {
                        // https://www.javacodemonk.com/md5-and-sha256-in-java-kotlin-and-android-96ed9628
                        "%02x".format(it)
                    }

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("ts", "$timeStamp")
                    .addQueryParameter("apikey", BuildConfig.MARVEL_PUBLIC_KEY)
                    .addQueryParameter("hash", hash)
                    .build()

                val requestBuilder = originalRequest.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                chain.proceed(request)
            }
        )

        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }

    @Provides
    @Singleton
    fun provideMarvelRepository(api: MarvelApi) = MarvelRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideMarvelApi(): MarvelApi
    {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .baseUrl(BASE_URL)
            .build()
            .create(MarvelApi::class.java)
    }
}