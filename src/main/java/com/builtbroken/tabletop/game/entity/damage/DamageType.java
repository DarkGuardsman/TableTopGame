package com.builtbroken.tabletop.game.entity.damage;

import com.builtbroken.tabletop.game.GameObject;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class DamageType extends GameObject
{
    /**
     * Default constructor
     *
     * @param uniqueID - unique ID for the type
     */
    public DamageType(String uniqueID)
    {
        super("damage.type." + uniqueID);
    }
}
