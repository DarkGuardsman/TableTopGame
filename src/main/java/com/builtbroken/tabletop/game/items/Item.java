package com.builtbroken.tabletop.game.items;

import com.builtbroken.tabletop.game.GameObject;
import com.builtbroken.tabletop.game.entity.Entity;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class Item extends GameObject
{
    /** Y-size of the inventory */
    public int height;
    /** X-size of the inventory */
    public int width;
    /** Mass of the item, used for strength checks and other minor math values */
    public int mass;

    public Item(String uniqueID)
    {
        super("item." + uniqueID);
    }

    /**
     * Does the item have an action
     * that an entity can use
     *
     * @return
     */
    public boolean hasAction(Entity entity)
    {
        return false;
    }
}
