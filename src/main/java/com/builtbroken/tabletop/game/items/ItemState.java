package com.builtbroken.tabletop.game.items;

/**
 * Object used to keep track of information about a current item state
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class ItemState
{
    /** Item data, same over all items of same type */
    public Item item;
    /** Number of items in the stack */
    public int stackSize = 1;
    /** Rotation on the map or in the inventory */
    public ItemRotation rotation = ItemRotation.LEFT;

    public ItemState(Item item)
    {
        this.item = item;
    }

    public ItemState(Item item, int amount)
    {
        this.item = item;
        this.stackSize = amount;
    }

    /**
     * Enum of rotation values
     */
    public enum ItemRotation
    {
        UP,
        LEFT,
        DOWN,
        RIGHT;
    }
}
