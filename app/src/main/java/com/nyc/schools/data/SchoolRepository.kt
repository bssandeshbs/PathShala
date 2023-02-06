package com.nyc.schools.data

import com.nyc.schools.data.local.SchoolLocalDataSource
import com.nyc.schools.data.model.SatScores
import com.nyc.schools.data.model.SchoolInfo
import com.nyc.schools.data.remote.SchoolRemoteDataSource
import javax.inject.Inject

/*
 * SchoolRepository decides where to fetch the data from. Initially, makes a call to local db
 * If records are not present, fetches from remote call
 * Once data is received from remote, stores data in local db for future use
 */
class SchoolRepository @Inject constructor(
    private val localDataSource: SchoolLocalDataSource,
    private val remoteDataSource: SchoolRemoteDataSource
) {

    suspend fun getAllSchools(): List<SchoolInfo>? {
        return remoteDataSource.getAllSchools()
    }

    suspend fun getSchoolsMyLimit(limit: Int, offset: Int): List<SchoolInfo>? {
        val result = localDataSource.getSchoolsMyLimit(limit, offset)
        return if (!result.isNullOrEmpty()) {
            result
        } else {
            val schoolInfo = remoteDataSource.getSchoolsMyLimit(limit, offset)
            // store schoolInfo in local db for future
            localDataSource.insertSchools(schoolInfo)
            return schoolInfo
        }
    }

    suspend fun getSATScoresForSchool(dbn: String): SatScores? {
        return localDataSource.getSATScoresForSchool(dbn) ?: run {
            val scores = remoteDataSource.getSATScoresForSchool(dbn)
            return localDataSource.insertSatScore(scores)
        }
    }

    suspend fun getSchoolsBySearch(query: String): List<SchoolInfo>? {
        //TODO: Currently I am searching schools from local database.
        // Hence it will search from only downloaded entities
        // Future enhancement would be pass the query to the network API
        // and get the results from network
        return localDataSource.getSchoolsBySearch(query)
    }
}
