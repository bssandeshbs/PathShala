package com.nyc.schools.data

import com.nyc.schools.data.model.SatScores
import com.nyc.schools.data.model.SchoolInfo

interface SchoolDataSource {

    suspend fun getAllSchools(): List<SchoolInfo>?

    suspend fun getSchoolsMyLimit(limit: Int, offset: Int): List<SchoolInfo>?

    suspend fun getSATScoresForSchool(dbn: String): SatScores?

    suspend fun getSchoolsBySearch(query: String): List<SchoolInfo>?
}