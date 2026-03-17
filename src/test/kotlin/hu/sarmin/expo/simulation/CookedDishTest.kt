package hu.sarmin.expo.simulation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CookedDishTest {

    @Test
    fun `not finished before end time`() {
        val dish = CookedDish(PASTA, startTime = 0L)
        assertFalse(dish.isFinished(999L))
    }

    @Test fun `finished exactly at end time`() {
        val dish = CookedDish(PASTA, startTime = 0L)
        assertTrue(dish.isFinished(1000L))
    }

    @Test fun `finished after end time`() {
        val dish = CookedDish(PASTA, startTime = 0L)
        assertTrue(dish.isFinished(5000L))
    }

    @Test fun `plate throws when dish is not ready`() {
        val dish = CookedDish(PASTA, startTime = 0L)
        assertFailsWith<IllegalStateException> { dish.plate(999L) }
    }

    @Test fun `plate returns correct PlatedDish when ready`() {
        val dish = CookedDish(PASTA, startTime = 0L)
        val plated = dish.plate(1000L)
        assertEquals(PASTA, plated.dish.dishType)
    }

    @Test fun `end time is relative to start time`() {
        val dish = CookedDish(PASTA, startTime = 500L)
        assertFalse(dish.isFinished(1499L))
        assertTrue(dish.isFinished(1500L))
    }
}
