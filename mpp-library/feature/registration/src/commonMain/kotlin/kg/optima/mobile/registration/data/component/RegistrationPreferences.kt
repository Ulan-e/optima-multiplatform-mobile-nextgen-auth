package kg.optima.mobile.registration.data.component

interface RegistrationPreferences {

    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val REFERENCE_ID = "reference_id"
        const val PERSON_ID = "person_id"
        const val SESSION_ID = "session_id"
        const val LIVENESS_RESULT = "liveness_result"
    }

    var accessToken: String?
    var referenceId: String?
    var personId: String?
    var sessionId: String?
    var livenessResult: String?

    fun clearAll()
}