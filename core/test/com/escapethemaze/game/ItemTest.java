package com.escapethemaze.game;

import com.escapethemaze.game.entities.Item;
import org.junit.Test;
import static org.junit.Assert.*;

public class ItemTest {
    
    @Test
    public void testItemCreation() {
        Item item = new Item(0, true);
        assertNotNull(item);
        assertTrue(item.usesLeft > 0);
    }
    
    @Test
    public void testItemNames() {
        assertEquals("Wood", Item.getItemName(0));
        assertEquals("Stone", Item.getItemName(1));
        assertEquals("Wheat", Item.getItemName(2));
    }
    
    @Test
    public void testItemUsage() {
        Item item = new Item(5, true); // Axe
        int initialUses = item.usesLeft;
        item.use();
        assertEquals(initialUses - 1, item.usesLeft);
        assertTrue(item.isUsable());
    }
}
