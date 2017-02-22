package com.builtbroken.tabletop.game.items.weapons;

import com.builtbroken.tabletop.game.items.Item;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class Weapon extends Item
{
    public Weapon(String uniqueID)
    {
        super("weapon." + uniqueID);
    }
}
