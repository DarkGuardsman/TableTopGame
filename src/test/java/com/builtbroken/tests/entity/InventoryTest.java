package com.builtbroken.tests.entity;

import com.builtbroken.tabletop.game.entity.inventory.Inventory;
import com.builtbroken.tabletop.game.entity.inventory.Slot;
import com.builtbroken.tabletop.game.items.Item;
import com.builtbroken.tabletop.game.items.ItemState;
import junit.framework.TestCase;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/25/2017.
 */
public class InventoryTest extends TestCase
{
    public void testInit()
    {
        Inventory inventory = new Inventory(2, 4);
        assertEquals(2, inventory.width);
        assertEquals(4, inventory.height);
    }

    /**
     * Tests {@link Inventory#canFit(int, int, ItemState)}
     */
    public void testCanFit()
    {
        Inventory inventory = new Inventory(2, 4);
        Item item = new Item("tree");
        item.height = 3;
        item.width = 2;

        ItemState itemState = new ItemState(item);

        assertTrue(inventory.canFit(0, 0, itemState));
        assertTrue(inventory.canFit(0, 1, itemState));

        assertFalse(inventory.canFit(1, 0, itemState));
        assertFalse(inventory.canFit(1, 1, itemState));

        for (int xx = 0; xx < 2; xx++)
        {
            for (int yy = 2; yy < 4; yy++)
            {
                assertFalse(inventory.canFit(xx, yy, itemState));
            }
        }
    }

    public void testAddItem()
    {
        Inventory inventory = new Inventory(2, 4);
        Item item = new Item("tree");
        item.height = 3;
        item.width = 2;

        ItemState itemState = new ItemState(item);
        assertTrue(inventory.insertItem(0, 0, itemState));

        //Loop slots
        for (int xx = 0; xx < 2; xx++)
        {
            for (int yy = 0; yy < 4; yy++)
            {
                Slot slot = inventory.getSlot(xx, yy);
                assertNotNull(slot);

                //Test for item
                if(yy < 3)
                {
                    //Test for root item
                    if (xx == 0 && yy == 0)
                    {
                        assertSame(itemState, slot.item);
                    }
                    //Test for placeholders
                    else
                    {
                        assertNotNull(slot.rootPosition);
                        assertSame(itemState, slot.rootPosition.item);
                    }
                }
                //Test for empty slots
                else
                {
                    assertNotNull(slot);
                    assertNull(slot.item);
                    assertNull(slot.rootPosition);
                }
            }
        }
    }

    public void testRemoveItem()
    {
        Inventory inventory = new Inventory(2, 4);
        Item item = new Item("tree");
        item.height = 3;
        item.width = 2;

        ItemState itemState = new ItemState(item);
        assertTrue(inventory.insertItem(0, 0, itemState));
        assertSame(itemState, inventory.removeItem(0, 0));

        //Ensure entire inventory is empty
        for (int xx = 0; xx < 2; xx++)
        {
            for (int yy = 0; yy < 4; yy++)
            {
                Slot slot = inventory.getSlot(xx, yy);
                assertNotNull(slot);
                assertNull(slot.item);
                assertNull(slot.rootPosition);
            }
        }
    }
}
