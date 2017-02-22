package com.builtbroken.tabletop.game.entity;

import com.builtbroken.tabletop.game.items.ItemState;

/**
 * Non-living object placed into the world
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class EntityItem extends Entity
{
    /** Can the item be picked up */
    boolean canPickUP = false;
    /** Can the item be moved */
    boolean canMove = false;
    /** Can projectils pass though the object */
    boolean canShootThough = false;

    /** Item data used by this entity */
    public final ItemState item;

    public EntityItem(ItemState item)
    {
        super("item");
        this.item = item;
    }
}
