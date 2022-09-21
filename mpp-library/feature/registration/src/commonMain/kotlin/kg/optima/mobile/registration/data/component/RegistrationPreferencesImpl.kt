package kg.optima.mobile.registration.data.component

import kg.optima.mobile.storage.StorageRepository

class RegistrationPreferencesImpl(
    private val storageRepository: StorageRepository
) : RegistrationPreferences {

    override var accessToken: String?
        get() = storageRepository.getString(RegistrationPreferences.ACCESS_TOKEN)
        set(value) {
            storageRepository.putString(RegistrationPreferences.ACCESS_TOKEN, value.orEmpty())
        }

    override var referenceId: String?
        get() = storageRepository.getString(RegistrationPreferences.REFERENCE_ID)
        set(value) {
            storageRepository.putString(RegistrationPreferences.REFERENCE_ID, value.orEmpty())
        }
    override var personId: String?
        get() = storageRepository.getString(RegistrationPreferences.PERSON_ID)
        set(value) {
            storageRepository.putString(RegistrationPreferences.PERSON_ID, value.orEmpty())
        }

    override var sessionId: String?
        get() = storageRepository.getString(RegistrationPreferences.SESSION_ID)
        set(value) {
            storageRepository.putString(RegistrationPreferences.SESSION_ID, value.orEmpty())
        }

    override var hash: String?
        get() = storageRepository.getString(RegistrationPreferences.HASH)
        set(value) {
            storageRepository.putString(RegistrationPreferences.HASH, value.orEmpty())
        }

    override fun clearAll() {
        sessionId = null
        accessToken = null
        referenceId = null
        personId = null
        hash = null
    }

}