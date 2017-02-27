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
        if (y < 0 || y + state.item.height > height)
        {
            return false;
        }
        //Check if slots are empty
        for (int w = 0; w < state.item.width; w++)
        {
            for (int h = 0; h < state.item.height; h++)
            {
                Slot slot = getSlot(w + x, h + y);
                if (slot == null || slot.item != null || slot.rootPosition != null)
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tries to insert the item at the location
     *
     * @param x     - start x point
     * @param y     - start y point
     * @param state - item data to check for fit
     * @return true if the item was inserted, or partially inserted. Make sure
     * to check stack size on the item to see if all items were added. As ammo
     * and other consumables can be merged without fully inserted.
     */
    public boolean insertItem(int x, int y, ItemState state)
    {
        if (canFit(x, y, state))
        {
            //TODO implement item rotation
            Slot primarySlot = getSlot(x, y);
            primarySlot.item = state;
            primarySlot.rootPosition = null;
            for (int xx = 0; xx < state.item.width; xx++)
            {
                for (int yy = 0; yy < state.item.height; yy++)
                {
                    Slot slot = getSlot(xx + x, yy + y);
                    if (slot != primarySlot)
                    {
                        slot.item = null;
                        slot.rootPosition = primarySlot;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Tries to insert the item in the first location it can find
     *
     * @param state
     * @return
     */
    public boolean insertItem(ItemState state)
    {
        return false;
    }

    /**
     * Called to remove the item at the location
     *
     * @param x - start x point
     * @param y - start y point
     * @return previous item in the slot
     */
    public ItemState removeItem(int x, int y)
    {
        Slot primarySlot = getSlot(x, y);
        if (primarySlot != null)
        {
            //TODO implement item rotation
            ItemState state = primarySlot.item;
            if (state != null)
            {
                for (int xx = 0; xx < state.item.width; xx++)
                {
                    for (int yy = 0; yy < state.item.height; yy++)
                    {
                        Slot slot = getSlot(xx + x, yy + y);
                        if (slot != primarySlot)
                        {
                            slot.item = null;
                            slot.rootPosition = null;
                        }
                    }
                }
            }
            primarySlot.item = null;
            primarySlot.rootPosition = null;
            return state;
        }
        return null;
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
        //Ensure we are inside of the expected array
        if (x >= 0 && x < width && y >= 0 && y < height)
        {
            //If array for x is empty init
            if (items == null)
            {
                items = new Slot[width][height];
            }
            //If array for y is empty init
            if (items[x] == null)
            {
                items[x] = new Slot[height];
            }
            //If slot is null init
            if (items[x][y] == null)
            {
                items[x][y] = new Slot(x, y);
            }
            //return slot
            return items[x][y];
        }
        return null;
    }
}
