package com.example.rit.data.network

data class ApiErrorMessage(val code: Int = -1, val message: String = "")

sealed class NetworkErrors : Throwable() {

    sealed class Http : NetworkErrors() {
        data class Unauthorized(val apiErrorMessage: ApiErrorMessage) : Http()
        data class NotFound(val apiErrorMessage: ApiErrorMessage) : Http()
        data class Timeout(val apiErrorMessage: ApiErrorMessage) : Http()
        data class LimitRateSuppressed(val apiErrorMessage: ApiErrorMessage) : Http()
        data class InternalServerError(val apiErrorMessage: ApiErrorMessage) : Http()
        data class BadRequest(val apiErrorMessage: ApiErrorMessage) : Http()
        data class Generic(val apiErrorMessage: ApiErrorMessage) : Http()
    }

    sealed class Connectivity : NetworkErrors() {
        object Timeout : Connectivity()
        object HostUnreachable : Connectivity()
        object FailedConnection : Connectivity()
        object BadConnection : Connectivity()
        object Generic : Connectivity()
    }

    sealed class Cancellation : NetworkErrors() {
        object Cancelled : Cancellation()
    }
}