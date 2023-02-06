package com.nyc.schools.data.remote

import com.nyc.schools.data.SchoolDataSource
import com.nyc.schools.data.local.SchoolDao
import com.nyc.schools.data.model.SatScores
import com.nyc.schools.data.model.SchoolInfo
import com.nyc.schools.network.Connectivity
import com.nyc.schools.network.CoroutinesDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SchoolRemoteDataSource @Inject constructor(
    private val schoolApi: NycSchoolApi,
    private val provider: CoroutinesDispatcherProvider,
    private val connectivity: Connectivity
) : SchoolDataSource {
    override suspend fun getAllSchools(): List<SchoolInfo>? {
        // TODO: Handle timeout in future, possibly use withTimeOut or other construct
        return withContext(provider.io) {
            if (!connectivity.isNetworkAvailable()) {
                throw NycSchoolApiException(RemoteError.NoNetworkError())
            }
            val response = schoolApi.getAllSchools().execute()
            if (response.code() != 200) {
                throw NycSchoolApiException(RemoteError.RetrofitException(response.code()))
            } else {
                return@withContext response.body()
            }
        }
    }

    override suspend fun getSchoolsMyLimit(limit: Int, offset: Int): List<SchoolInfo>? {
        return withContext(provider.io) {
            if (!connectivity.isNetworkAvailable()) {
                throw NycSchoolApiException(RemoteError.NoNetworkError())
            }
            val response = schoolApi.getSchoolsByLimit(limit, offset).execute()
            if (response.code() != 200) {
                throw NycSchoolApiException(RemoteError.RetrofitException(response.code()))
            } else {
                return@withContext response.body()
            }
        }
    }

    override suspend fun getSATScoresForSchool(dbn: String): SatScores? {
        return withContext(provider.io) {
            if (!connectivity.isNetworkAvailable()) {
                throw NycSchoolApiException(RemoteError.NoNetworkError())
            }
            val response = schoolApi.getSatDataBySchool(dbn).execute()
            if (response.code() != 200) {
                throw NycSchoolApiException(RemoteError.RetrofitException(response.code()))
            } else {
                if (response.body() != null && response.body()?.size == 1) {
                    return@withContext response.body()!![0]
                } else {
                    throw NycSchoolApiException(RemoteError.JsonParseError())
                }
            }
        }
    }

    override suspend fun getSchoolsBySearch(query: String): List<SchoolInfo>? {
        // TODO currently search is only supported against local db
        // In future add support to fetch from remote passing the query
        return null
    }
}