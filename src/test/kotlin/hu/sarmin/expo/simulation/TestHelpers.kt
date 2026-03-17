package hu.sarmin.expo.simulation

import hu.sarmin.expo.model.DishType
import hu.sarmin.expo.model.Menu

val PASTA  = DishType("Spaghetti")
val STEAK  = DishType("Steak")
val SALAD  = DishType("Caesar Salad")
val MENU   = Menu(setOf(PASTA, STEAK, SALAD))

fun createContext() = Context(Clock())

fun testKitchen(ctx: Context, pastaCapacity: Int = 2, grillCapacity: Int = 1) =
    Kitchen.Builder(ctx, MENU)
        .addStation("Pasta",  listOf(PASTA),  pastaCapacity)
        .addStation("Grill",  listOf(STEAK),  grillCapacity)
        .addStation("Cold",   listOf(SALAD),  1)
        .build()
