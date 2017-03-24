package com.builtbroken.tabletop.game.entity.controller;

import com.builtbroken.tabletop.game.entity.living.EntityLiving;

/**
 * What controls the entity and how it functions
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class Controller
{
    /** Number of actions that controller can do, normally 2 */
    int actionPoints;
    /** Number of tiles that the unit can move before using an action point */
    int movementPoints;

    boolean allowMovement = true;

    public EntityLiving entity;

    public final String id;
    public final String displayName;

    public Controller(String id, String displayName)
    {
        this.id = id;
        this.displayName = displayName;
    }

    public void update(double delta)
    {

    }
}
