package com.nyc.schools

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nyc.schools.data.local.SchoolDao
import com.nyc.schools.data.local.SchoolDatabase
import com.nyc.schools.data.model.SchoolInfo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SchoolDatabaseTest {

    private lateinit var database: SchoolDatabase
    private lateinit var schoolDao: SchoolDao

    lateinit var schoolInfo1: SchoolInfo
    lateinit var schoolInfo2: SchoolInfo

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            SchoolDatabase::class.java
        ).allowMainThreadQueries().build()

        schoolInfo1 = SchoolInfo(
            "db21", "Test School 1", "Syracuse", "9 /12", "Sy", "My school", "chase.com"
        )

        schoolInfo2 = SchoolInfo(
            "db2n2", "Test School 2", "Sunnyvale", "9 /12", "Sy", "My school", "chase.com"
        )

        schoolDao = database.schoolDao
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertSchools() = runBlocking {
        schoolDao.insertSchoolInfo(schoolInfo1)
        schoolDao.insertSchoolInfo(schoolInfo2)

        val schoolList = schoolDao.getAllSchools()
        assert(schoolList != null)
    }

    @Test
    fun validateSchoolInfo() = runBlocking {
        schoolDao.insertSchoolInfo(schoolInfo1)
        schoolDao.insertSchoolInfo(schoolInfo2)

        val schoolList = schoolDao.getAllSchools()
        assert(schoolList != null && schoolList.size == 2)

        assert(schoolList != null && schoolList[0].dbn == "db21")

        assert(schoolList != null && schoolList[0].schoolName == "Test School 1")

        assert(schoolList != null && schoolList[1].overview == "My school")
    }

    @Test
    fun querySchoolInfo() = runBlocking {
        schoolDao.insertSchoolInfo(schoolInfo1)
        schoolDao.insertSchoolInfo(schoolInfo2)

        val queryString = "Test School"
        val queryString2 = "Sunnyvale"

        val schoolList = schoolDao.searchSchools(queryString)
        assert(schoolList != null && schoolList.size == 2)
        assert(schoolList != null && schoolList[0].dbn == "db21")

        val schoolList2 = schoolDao.searchSchools(queryString2)
        assert(schoolList2 != null && schoolList2.size == 1)
        assert(schoolList2 != null && schoolList2[0].dbn == "db2n2")
    }
}