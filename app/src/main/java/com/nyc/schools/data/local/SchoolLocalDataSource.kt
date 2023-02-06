package com.nyc.schools.data.local

import android.util.Log
import com.nyc.schools.data.SchoolDataSource
import com.nyc.schools.data.model.SatScores
import com.nyc.schools.data.model.SchoolInfo
import com.nyc.schools.network.CoroutinesDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SchoolLocalDataSource @Inject constructor(
    private val schoolDao: SchoolDao,
    private val provider: CoroutinesDispatcherProvider
) : SchoolDataSource {

    suspend fun insertSchools(list: List<SchoolInfo>?) {
        list?.forEach { schoolInfo ->
            schoolDao.insertSchoolInfo(schoolInfo)
        }
    }

    suspend fun insertSatScore(satScores: SatScores?): SatScores? {
        satScores?.let {
            schoolDao.insertSATData(satScores)
        }
        return satScores
    }

    override suspend fun getAllSchools(): List<SchoolInfo>? {
        return withContext(provider.io) {
            return@withContext schoolDao.getAllSchools()
        }
    }

    override suspend fun getSchoolsMyLimit(limit: Int, offset: Int): List<SchoolInfo>? {
        return withContext(provider.io) {
            return@withContext schoolDao.getSchoolsByLimit(limit, offset)
        }
    }

    override suspend fun getSATScoresForSchool(dbn: String): SatScores? {
        return withContext(provider.io) {
            return@withContext schoolDao.getSatScore(dbn)
        }
    }

    override suspend fun getSchoolsBySearch(query: String): List<SchoolInfo>? {
        return withContext(provider.io) {
            return@withContext schoolDao.searchSchools(query)
        }
    }
}