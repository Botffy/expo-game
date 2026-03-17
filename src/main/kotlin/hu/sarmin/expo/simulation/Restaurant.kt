package hu.sarmin.expo.simulation

import hu.sarmin.expo.model.DishType
import hu.sarmin.expo.model.Menu

class Restaurant {
    private val menu: Menu
    private val kitchen: Kitchen

    constructor(context: Context) {
        val spaghetti = DishType("Spaghetti Bolognese")
        val steak = DishType("Steak")
        val caesarSalad = DishType("Caesar Salad")

        menu = Menu(setOf(spaghetti, steak, caesarSalad))

        kitchen = Kitchen.Builder(context, menu)
            .addStation("Pasta", listOf(spaghetti), 2)
            .addStation("Grill", listOf(steak), 3)
            .addStation("Salad", listOf(caesarSalad), 1)
            .build()
    }
}