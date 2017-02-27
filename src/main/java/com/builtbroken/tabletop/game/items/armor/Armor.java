package com.builtbroken.tabletop.game.items.armor;

import com.builtbroken.tabletop.game.items.Item;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class Armor extends Item
{
    public Armor(String uniqueID)
    {
        super("armor." + uniqueID);
    }

    public enum ArmorSlot
    {
        /** Full armor set that takes up head, chest, legs, glove, boots */
        SUIT,
        HEAD,
        CHEST,
        LEGS,
        GLOVES,
        BOOTS;
    }
}
