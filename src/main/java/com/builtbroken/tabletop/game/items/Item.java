package com.builtbroken.tabletop.game.items;

import com.builtbroken.tabletop.game.GameObject;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class Item extends GameObject
{
    /** length of the item in the inventory */
    int lenght;
    /** Width of the item in the inventory */
    int width;
    /** Mass of the item */
    int mass;

    public Item(String uniqueID)
    {
        super("item." + uniqueID);
    }
}
