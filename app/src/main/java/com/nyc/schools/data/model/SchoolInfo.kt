package com.nyc.schools.data.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "SchoolInfo")
data class SchoolInfo(

    @PrimaryKey
    @NonNull
    @Expose
    @SerializedName("dbn")
    val dbn: String,

    @Expose
    @SerializedName("school_name")
    val schoolName: String,

    @Expose
    @SerializedName("neighborhood")
    val neighborhood: String,

    @Expose
    @SerializedName("finalgrades")
    val finalGrades: String,

    @Expose
    @SerializedName("city")
    val city: String,

    @Expose
    @SerializedName("overview_paragraph")
    val overview: String,

    @Expose
    @SerializedName("website")
    val website: String
) : Parcelable