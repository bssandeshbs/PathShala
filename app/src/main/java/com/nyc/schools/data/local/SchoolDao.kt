package com.nyc.schools.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nyc.schools.data.model.SatScores
import com.nyc.schools.data.model.SchoolInfo

/*
 SchoolDao Provides all the API's to fetch data from database
 */
@Dao
interface SchoolDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSchoolInfo(schoolInfo: SchoolInfo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSATData(satScores: SatScores)

    //TODO: Currently, all these API's return just List or Data Object,
    // In future, I would up upgrade them to Flow or Coroutines

    @Query("SELECT * FROM SchoolInfo")
    suspend fun getAllSchools(): List<SchoolInfo>?

    @Query("SELECT * FROM SchoolInfo WHERE schoolName LIKE '%' " +
            "|| :query || '%' OR neighborhood LIKE '%' " +
            "|| :query || '%' OR city LIKE '%' " +
            "|| :query || '%' OR finalGrades LIKE '%'" +
            "|| :query || '%'")
    suspend fun searchSchools(query: String): List<SchoolInfo>?

    @Query("SELECT * FROM SchoolInfo LIMIT :limit OFFSET :offset")
    suspend fun getSchoolsByLimit(limit: Int, offset: Int): List<SchoolInfo>?

    @Query("SELECT * FROM SatScores WHERE dbn = :dbn")
    suspend fun getSatScore(dbn: String): SatScores?
}