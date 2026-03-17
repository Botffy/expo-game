package hu.sarmin.expo.simulation

import hu.sarmin.expo.model.DishType
import kotlin.test.*

class KitchenTest {

    private lateinit var context: Context
    private lateinit var kitchen: Kitchen

    @BeforeTest fun setUp() {
        context = createContext()
        kitchen = testKitchen(context)
    }

    @Test fun `firing an unknown dish throws`() {
        assertFailsWith<IllegalArgumentException> { kitchen.fire(DishType("Pizza")) }
    }

    @Test fun `nothing is plated before prep time is up`() {
        kitchen.fire(PASTA)
        context.clock.tick(999L)
        assertTrue(kitchen.update().isEmpty())
    }

    @Test fun `dish is plated exactly when prep time elapses`() {
        kitchen.fire(PASTA)
        context.clock.tick(1000L)
        val plated = kitchen.update()
        assertEquals(1, plated.size)
        assertEquals(PASTA, plated.first().dish.dishType)
    }

    @Test fun `multiple dishes within capacity are plated together`() {
        kitchen.fire(PASTA)
        kitchen.fire(PASTA)
        context.clock.tick(1000L)
        assertEquals(2, kitchen.update().size)
    }

    @Test fun `dishes from different stations are plated independently`() {
        kitchen.fire(PASTA)
        kitchen.fire(STEAK)
        context.clock.tick(1000L)
        val plated = kitchen.update()
        assertEquals(2, plated.size)
        assertNotNull(plated.find { it.dish.dishType == PASTA })
        assertNotNull(plated.find { it.dish.dishType == STEAK })
    }

    @Test fun `excess dish waits when station is at capacity`() {
        val singleSlotKitchen = testKitchen(context, pastaCapacity = 1)

        singleSlotKitchen.fire(PASTA)  // → cooking
        singleSlotKitchen.fire(PASTA)  // → waiting

        // After first prep time: first dish plated, second moves to cooking
        context.clock.tick(1000L)
        val firstBatch = singleSlotKitchen.update()
        assertEquals(1, firstBatch.size, "Only the first dish should be plated")

        // After another prep time: second dish plated
        context.clock.tick(1000L)
        val secondBatch = singleSlotKitchen.update()
        assertEquals(1, secondBatch.size, "The queued dish should now be plated")
    }

    @Test fun `update returns empty when nothing was fired`() {
        context.clock.tick(5000L)
        assertTrue(kitchen.update().isEmpty())
    }

    @Test fun `update is idempotent - plated dishes are not returned twice`() {
        kitchen.fire(STEAK)
        context.clock.tick(1000L)
        assertEquals(1, kitchen.update().size)
        assertTrue(kitchen.update().isEmpty())  // same tick, nothing new
    }
}
