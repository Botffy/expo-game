package hu.sarmin.expo.simulation

import hu.sarmin.expo.model.DishType
import kotlin.test.Test
import kotlin.test.assertFailsWith

class KitchenBuilderTest {

    @Test
    fun `builds successfully when all dishes are assigned`() {
        testKitchen(createContext())
    }

    @Test fun `throws when station lists a dish not on the menu`() {
        val unknown = DishType("Pizza")
        assertFailsWith<IllegalArgumentException> {
            Kitchen.Builder(createContext(), MENU)
                .addStation("Misc", listOf(PASTA, unknown), 1)
        }
    }

    @Test fun `throws for duplicate station name`() {
        assertFailsWith<IllegalArgumentException> {
            Kitchen.Builder(createContext(), MENU)
                .addStation("Pasta", listOf(PASTA), 2)
                .addStation("Pasta", listOf(STEAK), 1)
        }
    }

    @Test fun `throws when a menu dish is not assigned to any station`() {
        assertFailsWith<IllegalArgumentException> {
            Kitchen.Builder(createContext(), MENU)
                .addStation("Pasta", listOf(PASTA), 2)
                .addStation("Grill", listOf(STEAK), 1)
                // SALAD is not assigned
                .build()
        }
    }

    @Test fun `throws for zero capacity`() {
        assertFailsWith<IllegalArgumentException> {
            Kitchen.Builder(createContext(), MENU)
                .addStation("Pasta", listOf(PASTA), 0)
        }
    }

    @Test fun `throws for negative capacity`() {
        assertFailsWith<IllegalArgumentException> {
            Kitchen.Builder(createContext(), MENU)
                .addStation("Pasta", listOf(PASTA), -1)
        }
    }

    @Test fun `throws for empty dish list`() {
        assertFailsWith<IllegalArgumentException> {
            Kitchen.Builder(createContext(), MENU)
                .addStation("Empty", emptyList(), 1)
        }
    }
}
