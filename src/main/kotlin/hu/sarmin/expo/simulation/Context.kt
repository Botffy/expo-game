package hu.sarmin.expo.simulation

import kotlin.random.Random

data class Clock(private var time: Long = 0L) {
    fun tick(delta: Long = 1L) {
        require(delta > 0) { "Delta must be positive, got $delta" }
        time += delta
    }
    fun time() = time
}

data class SimRandom(private val seed: Long) {
    private val random = Random(seed)
    fun nextInt(range: IntRange) = random.nextInt(range.first, range.last + 1)
}

data class Context(
    val clock: Clock,
    val random: SimRandom
)
