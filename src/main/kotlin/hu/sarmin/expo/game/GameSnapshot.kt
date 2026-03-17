package hu.sarmin.expo.game

/**
 * Immutable snapshot of simulation state for the UI.
 * The sim's mutable internals never cross this boundary.
 */
data class GameSnapshot(
    /** Current simulation clock time, in sim units. */
    val simTime: Long = 0L,
    /** Running list of dish names plated since session start (most recent last). */
    val platedDishes: List<String> = emptyList(),
)

