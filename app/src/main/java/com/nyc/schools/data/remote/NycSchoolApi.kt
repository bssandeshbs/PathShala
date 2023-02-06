package com.nyc.schools.data.remote

import com.nyc.schools.data.model.SatScores
import com.nyc.schools.data.model.SchoolInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
  API's to fetch data from NYC School API
 */
interface NycSchoolApi {
    /** RETROFIT CALL METHODS __________________________________________________________________  */
    @GET("s3k6-pzi2.json")
    fun getAllSchools(): Call<List<SchoolInfo>>

    @GET("s3k6-pzi2.json")
    fun getAllSchools(@Query("\$where") limit: Int): Call<List<SchoolInfo>>

    @GET("s3k6-pzi2.json?")
    fun getSchoolsByLimit(@Query("\$limit") limit: Int,
                          @Query("\$offset") offset: Int): Call<List<SchoolInfo>>

    @GET("f9bf-2cp4.json?")
    fun getSatDataBySchool(@Query("dbn") dbnId: String): Call<List<SatScores>>
}