package com.builtbroken.tabletop.game.items.gear;

import com.builtbroken.tabletop.game.items.Item;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class Gear extends Item
{
    public Gear(String uniqueID, int id)
    {
        super("gear." + uniqueID, id);
    }

    public enum GearType
    {
        HELD,
        HELM,
        ARM,
        BACK
    }
}
