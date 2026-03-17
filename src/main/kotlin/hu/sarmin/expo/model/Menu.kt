package hu.sarmin.expo.model

data class Menu(val dishes: Set<DishType>) {

    fun contains(dish: DishType) = dishes.contains(dish)
}