package com.nyc.schools.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nyc.schools.data.model.SatScores
import com.nyc.schools.data.model.SchoolInfo

@Database(entities = [SchoolInfo::class, SatScores::class], version = 1, exportSchema = false)
abstract class SchoolDatabase : RoomDatabase() {
    abstract val schoolDao: SchoolDao

    companion object {
        private const val DATABASE_NAME = "school_database"
        fun createSchoolDatabase(context: Context): SchoolDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                SchoolDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}