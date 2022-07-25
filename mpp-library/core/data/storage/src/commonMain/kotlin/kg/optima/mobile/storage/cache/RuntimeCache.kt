package kg.optima.mobile.storage.cache

import co.touchlab.stately.concurrency.AtomicReference
import kg.optima.mobile.storage.model.profile.Profile

class RuntimeCache {

    private val profile = AtomicReference<Profile?>(null)

    fun setProfile(profile: Profile?) {
        this.profile.set(profile)
    }

    fun getProfile(): Profile? {
        return profile.get()
    }
}