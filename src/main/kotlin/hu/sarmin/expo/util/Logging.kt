package hu.sarmin.expo.util

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * To be used as a companion object:
 *
 * ```
 * companion object : WithLogging()
 * ```
 *
 * Automatically adds uses the enclosing class name to identify the logger
 */
abstract class WithLogging {
    val logger: KLogger by lazy { logger() }

    fun logger(): KLogger {
        return KotlinLogging.logger(this.javaClass.enclosingClass?.name ?: this.javaClass.name)
    }
}
