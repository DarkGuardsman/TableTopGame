package com.builtbroken.tabletop.game.entity.inventory;

import com.builtbroken.tabletop.game.items.ItemState;

/**
 * Simple 2D inventory implementation
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/25/2017.
 */
public class Inventory
{
    /** Width of the inventory */
    public final int width;
    /** Height of the inventory */
    public final int height;
    /** Inventory slot objects */
    protected Slot[][] items;

    public Inventory(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the item at the current index in the inventory.
     * <p>
     * Try not to use this unless dumping the entire array.
     *
     * @param slot - index
     * @return item, or null if outside of the inventory
     */
    public ItemState getItem(int slot)
    {
        return null; //TODO implement
    }

    /**
     * Gets the item at the current location in the inventory matrix
     *
     * @param x - width value
     * @param y - height value
     * @return item, or null if outside of inventory
     */
    public ItemState getItem(int x, int y)
    {
        final Slot slot = getSlot(x, y);
        if (slot != null)
        {
            if (slot.item != null)
            {
                return slot.item;
            }
            else if (slot.rootPosition != null)
            {
                return slot.rootPosition.item;
            }
        }
        return null;
    }

    /**
     * Checks to see if the item can fit into the slot
     *
     * @param x
     * @param y
     * @param state - item state being inserted
     */
    public boolean canFit(int x, int y, ItemState state)
    {
        //TODO implement item rotation
        if (x < 0 || x + state.item.width > width)
        {
            return false;
        }
        if (y < 0 || y + state.item.height > width)
        {
            return false;
        }
        //Check if slots are empty
        for (int xx = 0; xx < state.item.width; xx++)
        {
            for (int yy = 0; yy < state.item.height; yy++)
            {

            }
        }
        return false;
    }

    public void setSlot(int x, int y, ItemState state)
    {

    }

    /**
     * Gets the slot at the location in the inventory.
     * <p>
     * Do not modify the slot directly. Use the provided
     * set and get methods to events/logic trigger correctly.
     *
     * @param x - width value
     * @param y - height value
     * @return slot at location, or null if outside of inventory
     */
    public Slot getSlot(int x, int y)
    {
        if (items == null)
        {
            items = new Slot[width][height];
        }
        if (items != null && x >= 0 && x < items.length && items[x] != null && y >= 0 && y < items[x].length)
        {
            if (items[x][y] == null)
            {
                items[x][y] = new Slot(x, y);
            }
            return items[x][y];
        }
        return null;
    }
}
