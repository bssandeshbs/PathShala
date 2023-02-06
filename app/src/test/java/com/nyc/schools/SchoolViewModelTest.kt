package com.nyc.schools

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nyc.schools.data.SchoolRepository
import com.nyc.schools.data.model.SatScores
import com.nyc.schools.data.model.SchoolInfo
import com.nyc.schools.ui.SchoolViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SchoolViewModelTest : BaseTestClass() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineRule()

    @MockK
    lateinit var schoolRepository: SchoolRepository

    @MockK
    lateinit var schoolViewModel: SchoolViewModel

    lateinit var schoolInfo1: SchoolInfo
    lateinit var schoolInfo2: SchoolInfo

    lateinit var satScores1: SatScores
    lateinit var satScores2: SatScores

    @Before
    fun setup() {
        val application = mockk<Application>(relaxed = true)
        schoolViewModel = SchoolViewModel(schoolRepository)

        schoolInfo1 = SchoolInfo(
            "db1n", "Test School 1", "Syracuse", "9 /12", "Sy", "My school", "google.com"
        )
        schoolInfo2 = SchoolInfo(
            "db2n2", "Test School 2", "Sunnyvale", "9 /12", "Sy", "My school", "google.com"
        )

        satScores1 = SatScores("db1n", "400", "500", "600", "700", "800")
        satScores2 = SatScores("db2n", "300", "700", "900", "200", "700")
    }

    @Test
    fun test_getSchoolsByLimit() = runTest {
        coEvery {
            schoolRepository.getSchoolsMyLimit(12, 0)
        } returns listOf(schoolInfo1, schoolInfo2)

        schoolViewModel.getSchoolList(true, 12, 0)
        schoolViewModel.schoolList().observeForever {
            assert(it?.size == 2)
            assert(it != null && it.size > 1 && it[0].dbn == "db1n")
        }
        delay(3000)
    }

    @Test
    fun test_getSatScore() = runTest {
        coEvery {
            schoolRepository.getSchoolsBySearch("db1n")
        } returns listOf(schoolInfo1)

        schoolViewModel.getSatScore("School 1")
        schoolViewModel.satScores().observeForever {
            assert(it != null && it.dbn == "db1n")
        }
        delay(3000)
    }

    @Test
    fun test_getSatScore_fail() = runTest {
        coEvery {
            schoolRepository.getSATScoresForSchool("db2n")
        } returns satScores1

        schoolViewModel.getSatScore("db2n")
        schoolViewModel.satScores().observeForever {
            assert(it != null && it.dbn != "db2n")
        }
        delay(3000)
    }

    @Test
    fun test_filterValue() = runTest {
        coEvery {
            schoolRepository.getSchoolsBySearch("Syracuse")
        } returns listOf(schoolInfo1)

        schoolViewModel.filterValues("Syracuse")
        schoolViewModel.satScores().observeForever {
            assert(it != null && it.dbn == "db1n")
        }
        delay(3000)
    }
}