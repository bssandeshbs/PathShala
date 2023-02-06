package com.nyc.schools.ui

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyc.schools.data.remote.NycErrorType
import com.nyc.schools.data.remote.NycSchoolApiException
import com.nyc.schools.data.remote.RemoteError
import com.nyc.schools.data.model.SatScores
import com.nyc.schools.data.model.SchoolInfo
import com.nyc.schools.data.SchoolRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class SchoolViewModel @Inject constructor(private val schoolRepository: SchoolRepository) :
    ViewModel() {

    private val schoolList = MutableLiveData<List<SchoolInfo>?>()
    fun schoolList() = schoolList

    private var allSchoolList = mutableListOf<SchoolInfo>()

    private val satScores = SingleLiveEvent<SatScores?>()

    // emit errors to view
    private val error = SingleLiveEvent<NycErrorType?>()
    fun error() = error

    // emit SAT Data
    fun satScores() = satScores
    var counter = 0

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val errorType = (exception as? NycSchoolApiException)?.errorType
        if (errorType != null) {
            // TODO: ErrorCause & ErrorCode is mostly used for analytics
            //  and error message is user friendly message
            Log.d("Exception", "Exception at ${errorType.errorCause}")
            when (errorType) {
                is RemoteError.NoNetworkError,
                is RemoteError.JsonParseError,
                is RemoteError.RetrofitException -> {
                    error.postValue(errorType)
                }
                else -> {
                    error.postValue(RemoteError.GenericError())
                }
            }
        }
    }

    fun getSavedSchools() {
        schoolList.postValue(allSchoolList)
    }

    /*
     Fetch schools list from either local database or remote network call
     The API fetches triggers a fetch only if the data is not present in local DB
     If data is not present in local DB, it fetches only 12 records per request.
     The limit parameter fetches the specified records
     The offset parameters fetches records from specified row
     */
    fun getSchoolList(forceFetch: Boolean = false, limit: Int = 12, offset: Int = counter) {
        if (schoolList.value == null || forceFetch) {
            viewModelScope.launch(exceptionHandler) {
                val result = schoolRepository.getSchoolsMyLimit(limit, offset)
                result?.let {
                    allSchoolList.addAll(it)
                    counter += 12
                    schoolList.postValue(allSchoolList)
                }
            }
        }
    }

    fun getSatScore(dbn: String) {
        viewModelScope.launch(exceptionHandler) {
            val result = schoolRepository.getSATScoresForSchool(dbn)
            satScores.postValue(result)
        }
    }

    fun filterValues(query: String) {
        viewModelScope.launch(exceptionHandler) {
            val result = schoolRepository.getSchoolsBySearch(query)
            if (result == null || result.isEmpty()) {
                schoolList.postValue(emptyList())
            } else {
                schoolList.postValue(result)
            }
        }
    }
}
