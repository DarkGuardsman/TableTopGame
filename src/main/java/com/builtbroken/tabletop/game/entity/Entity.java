package com.builtbroken.tabletop.game.entity;

import com.builtbroken.tabletop.game.GameObject;

/**
 * Something that is placed in the game world
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class Entity extends GameObject
{
    /** Can the entity take damage from attacks */
    boolean canTakeDamae = true;

    int x = 0;
    int y = 0;
    int z = 0;

    public Entity(String uniqueID)
    {
        super("entity." + uniqueID);
    }
}
