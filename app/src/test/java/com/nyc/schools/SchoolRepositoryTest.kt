package com.nyc.schools

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nyc.schools.data.SchoolRepository
import com.nyc.schools.data.local.SchoolLocalDataSource
import com.nyc.schools.data.model.SatScores
import com.nyc.schools.data.model.SchoolInfo
import com.nyc.schools.data.remote.SchoolRemoteDataSource
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SchoolRepositoryTest : BaseTestClass() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var schoolRepository: SchoolRepository

    @MockK
    lateinit var schoolLocalDataSource: SchoolLocalDataSource

    @MockK
    lateinit var schoolRemoteDataSource: SchoolRemoteDataSource

    lateinit var schoolInfo1: SchoolInfo
    lateinit var schoolInfo2: SchoolInfo

    lateinit var satScores1: SatScores
    lateinit var satScores2: SatScores

    @Before
    fun setup() {
        val application = mockk<Application>(relaxed = true)
        schoolRepository = SchoolRepository(schoolLocalDataSource, schoolRemoteDataSource)

        schoolInfo1 = SchoolInfo(
            "db2n", "Test School 1", "Syracuse", "9 /12", "Sy", "My school", "google.com"
        )

        schoolInfo2 = SchoolInfo(
            "db2n2", "Test School 2", "Syracuse", "9 /12", "Sy", "My school", "google.com"
        )

        satScores1 = SatScores("db1n", "400", "500", "600", "700", "800")

        satScores2 = SatScores("db2n", "300", "700", "900", "200", "700")

    }

    @Test
    fun test_fetchSchoolsByLimit() = runTest {
        coEvery {
            schoolLocalDataSource.getSchoolsMyLimit(12, 0)
        } returns listOf(schoolInfo1, schoolInfo2)

        val schools = schoolRepository.getSchoolsMyLimit(12, 0)
        assertTrue(schools != null && schools.size == 2)
    }

    @Test
    fun test_fetchSchoolsByLimit_Local_Fail() = runTest {
        coEvery {
            schoolLocalDataSource.getSchoolsMyLimit(12, 0)
        } returns emptyList()

        coEvery {
            schoolLocalDataSource.insertSchools(any())
        } answers {}

        coEvery {
            schoolRemoteDataSource.getSchoolsMyLimit(12, 0)
        } returns emptyList()

        val schools = schoolRepository.getSchoolsMyLimit(12, 0)
        assertTrue(schools == null || schools.isEmpty())
    }

    @Test
    fun test_fetchSchoolsByLimit_Remote_Pass() = runTest {
        coEvery {
            schoolLocalDataSource.getSchoolsMyLimit(12, 0)
        } returns emptyList()

        coEvery {
            schoolLocalDataSource.insertSchools(listOf(schoolInfo1, schoolInfo2))
        } answers {}

        coEvery {
            schoolRemoteDataSource.getSchoolsMyLimit(12, 0)
        } returns listOf(schoolInfo1, schoolInfo2)

        val schools = schoolRepository.getSchoolsMyLimit(12, 0)
        assertTrue(schools != null && schools.size == 2)
    }

    @Test
    fun test_fetchAllSchools() = runTest {
        coEvery {
            schoolRepository.getAllSchools()
        } returns listOf(schoolInfo1, schoolInfo2)

        val schools = schoolRepository.getAllSchools()
        assertTrue(schools != null && schools.size == 2)
    }

    @Test
    fun test_getSatScoresTest_Local_Successful() = runTest {
        coEvery {
            schoolLocalDataSource.getSATScoresForSchool(satScores1.dbn)
        } returns satScores1

        val satScores = schoolRepository.getSATScoresForSchool(satScores1.dbn)
        assertTrue(satScores != null && satScores.dbn == "db1n")
    }

    @Test
    fun test_getSatScoresTest_Local_Failure() = runTest {
        coEvery {
            schoolLocalDataSource.getSATScoresForSchool(satScores1.dbn)
        } returns satScores2

        val satScores = schoolRepository.getSATScoresForSchool(satScores1.dbn)
        assertFalse(satScores != null && satScores.dbn == "db1n")
    }
}