package hu.sarmin.expo.game

/**
 * Controls the mapping between real time and simulation time.
 *
 * The loop fires [ticksPerSecond] times per real second.
 * Each iteration advances the simulation clock by [simUnitsPerTick].
 *
 * Example defaults: 20 ticks/s × 50 units/tick = 1 000 sim-units/s,
 * so a dish with endTime = startTime + 1 000 cooks in exactly 1 real second.
 */
data class GameConfig(
    val ticksPerSecond: Double = 20.0,
    val simUnitsPerTick: Long = 50L,
) {
    init {
        require(ticksPerSecond > 0.0) { "ticksPerSecond must be positive, got $ticksPerSecond" }
        require(simUnitsPerTick > 0L) { "simUnitsPerTick must be positive, got $simUnitsPerTick" }
    }
}

