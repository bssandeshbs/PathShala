package com.nyc.schools.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "SatScores")
data class SatScores(
    @Expose
    @PrimaryKey
    @NonNull
    @SerializedName("dbn")
    val dbn: String,

    @Expose
    @SerializedName("school_name")
    val schoolName: String,

    @Expose
    @SerializedName("num_of_sat_test_takers")
    val satTestTakers: String,

    @Expose
    @SerializedName("sat_critical_reading_avg_score")
    val satReadingAvg: String,

    @Expose
    @SerializedName("sat_math_avg_score")
    val satMathAvg: String,

    @Expose
    @SerializedName("sat_writing_avg_score")
    val satWritingAvg: String
)