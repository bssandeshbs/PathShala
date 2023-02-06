package com.nyc.schools.di

import android.app.Application
import com.nyc.schools.data.*
import com.nyc.schools.data.local.SchoolDatabase
import com.nyc.schools.data.local.SchoolLocalDataSource
import com.nyc.schools.data.remote.NycSchoolApi
import com.nyc.schools.data.remote.SchoolRemoteDataSource
import com.nyc.schools.network.Connectivity
import com.nyc.schools.network.CoroutinesDispatcherProvider
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class SchoolModule {

    @Provides
    fun provideNycSchoolAPI(retrofit: Retrofit): NycSchoolApi {
        return retrofit.create(NycSchoolApi::class.java)
    }

    @Provides
    fun providesSchoolRepository(
        localDataSource: SchoolLocalDataSource,
        remoteDataSource: SchoolRemoteDataSource
    ): SchoolRepository {
        return SchoolRepository(localDataSource, remoteDataSource)
    }

    @Provides
    fun provideSchoolLocalDataSource(
        schoolDatabase: SchoolDatabase,
        coroutineDispatcher: CoroutinesDispatcherProvider
    ): SchoolLocalDataSource {
        return SchoolLocalDataSource(schoolDatabase.schoolDao, coroutineDispatcher)
    }

    @Provides
    fun provideSchoolRemoteDataSource(
        schoolApi: NycSchoolApi,
        coroutineDispatcher: CoroutinesDispatcherProvider,
        connectivity: Connectivity
    ): SchoolRemoteDataSource {
        return SchoolRemoteDataSource(schoolApi, coroutineDispatcher, connectivity)
    }

    @Provides
    fun provideNycSchoolDatabase(application: Application): SchoolDatabase {
        return SchoolDatabase.createSchoolDatabase(application)
    }


}