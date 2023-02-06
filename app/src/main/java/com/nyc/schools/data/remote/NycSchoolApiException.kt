package com.nyc.schools.data.remote

import com.nyc.schools.R

class NycSchoolApiException(val errorType: NycErrorType) : Exception()

interface NycErrorType {
    var errorCode: Int
    val errorCause: String
    val errorMessage: Int
}

sealed class RemoteError : NycErrorType {
    class NoNetworkError : RemoteError() {
        override var errorCode: Int = 2001
        override val errorCause: String = "No Network Connection Available!"
        override val errorMessage: Int = R.string.no_network
    }

    class JsonParseError : RemoteError() {
        override var errorCode: Int = 2002
        override val errorCause: String = "Error Parsing Incoming Json"
        override val errorMessage: Int = R.string.something_went_wrong
    }

    class RetrofitException(override var errorCode: Int) : RemoteError() {
        override val errorCause: String = "Error Parsing Incoming Json"
        override val errorMessage: Int = R.string.something_went_wrong
    }

    class GenericError : RemoteError() {
        override var errorCode: Int = 3000
        override val errorCause: String = "Unknown error"
        override val errorMessage: Int = R.string.generic_error
    }
}

