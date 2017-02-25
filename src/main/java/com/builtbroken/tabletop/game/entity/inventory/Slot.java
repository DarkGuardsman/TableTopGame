package com.builtbroken.tabletop.game.entity.inventory;

import com.builtbroken.tabletop.game.items.ItemState;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/25/2017.
 */
public class Slot
{
    /** Location in the inventory array x */
    public final int x;
    /** Location in the inventory array y */
    public final int y;

    /** Location of the core item, used if the item is several items in size */
    public Slot rootPosition;
    /** Item stored in the slot */
    public ItemState item;

    public Slot(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
