package hu.sarmin.expo.simulation

import hu.sarmin.expo.model.Dish
import hu.sarmin.expo.model.DishType
import hu.sarmin.expo.model.Menu
import hu.sarmin.expo.util.WithLogging

private class DishOrder(val dishType: DishType, val orderStart: Long)

class CookedDish(dishType: DishType, val startTime: Long) {
    val dish: Dish = Dish(dishType)

    val endTime: Long
        get() = startTime + 1000L

    fun isFinished(currentTime: Long) = currentTime >= endTime

    fun plate(currentTime: Long): PlatedDish {
        if (!isFinished(currentTime)) throw IllegalStateException("Dish is not ready yet: $dish")

        return PlatedDish(dish, currentTime)
    }
}

class PlatedDish(val dish: Dish, val platedAt: Long)

private class Station(val context: Context, val name: String, val dishes: List<DishType>, val capacity: Int) {
    companion object : WithLogging()

    private val cooking = mutableListOf<CookedDish>()
    private val waiting = ArrayDeque<DishOrder>()

    private fun hasCapacity() = cooking.size < capacity

    fun update(): List<PlatedDish> {
        val currentTime = context.clock.time()

        val platedDishes = mutableListOf<PlatedDish>()
        val iterator = cooking.iterator()
        while (iterator.hasNext()) {
            val dish = iterator.next()
            if (dish.isFinished(currentTime)) {
                platedDishes.add(dish.plate(currentTime))
                iterator.remove()
            }
        }

        if (platedDishes.isNotEmpty()) {
            logger.info { "Station $name: Plated dishes: ${platedDishes.map { it.dish.dishType.name }}" }
        }

        while (waiting.isNotEmpty() && hasCapacity()) {
            val nextOrder = waiting.removeFirst()
            cooking.add(CookedDish(nextOrder.dishType, currentTime))
        }

        return platedDishes
    }

    fun fire(dish: DishType) {
        if (hasCapacity()) {
            logger.info { "Station $name: Firing ${dish.name}" }
            cooking.add(CookedDish(dish, context.clock.time()))
        } else {
            logger.info { "Station $name: ${dish.name} is waiting (station at capacity)" }
            waiting.addLast(DishOrder(dish, context.clock.time()))
        }
    }
}

class Kitchen private constructor(private val context: Context, private val menu: Menu, private val stations: List<Station>) {
    private val routing = stations
        .flatMap { station -> station.dishes.map { dish -> dish to station } }
        .toMap()

    fun fire(dish: DishType) {
        val station = routing[dish]
            ?: throw IllegalArgumentException("Dish not assigned to any station: $dish")
        station.fire(dish)
    }

    fun update(): List<PlatedDish> = stations.flatMap { it.update() }

    class Builder(val context: Context, val menu: Menu) {
        private val stations = mutableListOf<Station>()

        fun addStation(name: String, dishes: List<DishType>, capacity: Int): Builder {
            require(capacity > 0) { "Station capacity must be at least 1, got $capacity for '$name'" }
            require(dishes.isNotEmpty()) { "Station '$name' must handle at least one dish type" }
            dishes
                .filter { !menu.contains(it) }
                .joinToString(", ")
                .let { if (it.isNotEmpty()) throw IllegalArgumentException("Unknown dish: $it") }

            stations.filter { it.dishes.intersect(dishes).isNotEmpty() }
                .joinToString(", ") { it.name }
                .let { if (it.isNotEmpty()) throw IllegalArgumentException("Dish already assigned to another station: $it") }

            if (stations.any { it.name == name }) throw IllegalArgumentException("Station already exists: $name")

            stations.add(Station(context, name, dishes, capacity))

            return this
        }

        fun build(): Kitchen {
            menu.dishes
                .filter { dish -> stations.none { it.dishes.contains(dish) } }
                .joinToString(", ")
                .let { if (it.isNotEmpty()) throw IllegalArgumentException("Dishes not assigned to any station: $it") }

            return Kitchen(context, menu, stations.toList())
        }
    }
}
