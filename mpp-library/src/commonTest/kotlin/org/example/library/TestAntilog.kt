/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package kg.optima.mobile

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel

internal class TestAntilog : Antilog() {
    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        buildString {
            append(priority.name)
            append(' ')

            if (tag != null) {
                append('[')
                append(tag)
                append("]: ")
            }
            if (message != null) {
                append(message)
            }
        }.let(::println)

        throwable?.printStackTrace()
    }
}
