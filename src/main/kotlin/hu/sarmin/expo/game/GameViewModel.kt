package hu.sarmin.expo.game

import hu.sarmin.expo.simulation.Clock
import hu.sarmin.expo.simulation.Context
import hu.sarmin.expo.simulation.Restaurant
import hu.sarmin.expo.simulation.SimRandom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.TimeSource
import kotlin.time.measureTime

/** Fire the spaghetti this many loop-ticks after start (~0.5 s at default config). */
private const val SPAGHETTI_FIRE_TICK = 10L

/** Keep the last N plated entries in the snapshot. */
private const val MAX_PLATED_LOG = 50

class GameViewModel(scope: CoroutineScope, private val config: GameConfig = GameConfig()) {

    private val context = Context(Clock(), SimRandom(Random.Default.nextLong()))
    private val restaurant = Restaurant(context)

    private val _state = MutableStateFlow(GameSnapshot())
    val state: StateFlow<GameSnapshot> = _state.asStateFlow()

    init {
        scope.launch(Dispatchers.Default) {
            val msPerTick = (1_000.0 / config.ticksPerSecond).toLong().coerceAtLeast(1L)
            val platedLog = mutableListOf<String>()
            var loopTick = 0L
            var spaghettiLaunched = false

            while (isActive) {
                val elapsed = TimeSource.Monotonic.measureTime {
                    // Schedule a spaghetti shortly after launch
                    if (!spaghettiLaunched && loopTick >= SPAGHETTI_FIRE_TICK) {
                        val spaghetti = restaurant.dish("Spaghetti Bolognese")
                        restaurant.fire(spaghetti)
                        spaghettiLaunched = true
                    }

                    context.clock.tick(config.simUnitsPerTick)
                    val plated = restaurant.update()
                    loopTick++

                    if (plated.isNotEmpty()) {
                        platedLog += plated.map { it.dish.dishType.name }
                        if (platedLog.size > MAX_PLATED_LOG) {
                            platedLog.subList(0, platedLog.size - MAX_PLATED_LOG).clear()
                        }
                    }

                    _state.value = GameSnapshot(
                        simTime = context.clock.time(),
                        platedDishes = platedLog.toList(),
                    )
                }

                val remaining = msPerTick - elapsed.inWholeMilliseconds
                if (remaining > 0) delay(remaining)
            }
        }
    }
}
