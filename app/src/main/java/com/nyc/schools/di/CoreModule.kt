package com.nyc.schools.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.nyc.schools.network.Connectivity
import com.nyc.schools.network.CoroutinesDispatcherProvider
import com.nyc.schools.network.NetworkInterceptor
import com.nyc.schools.ui.Constants
import com.nyc.schools.ui.Constants.READ_TIMEOUT
import com.nyc.schools.ui.Constants.WRITE_TIMEOUT
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class CoreModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    private val client: OkHttpClient = OkHttpClient.Builder() // establish connection to server
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS) // time between each byte sent to server
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .addInterceptor(NetworkInterceptor())
        .build()

    @Singleton
    @Provides
    fun provideCoroutinesDispatcherProvider() =
        CoroutinesDispatcherProvider(
            Dispatchers.Main,
            Dispatchers.IO,
            Dispatchers.Default)

    @Provides
    @Singleton
    fun providesConnectivityManager(application: Application): ConnectivityManager {
        return application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideConnectivity(application: Application, connectivityManager: ConnectivityManager): Connectivity {
        return Connectivity(connectivityManager, application.applicationContext)
    }

}