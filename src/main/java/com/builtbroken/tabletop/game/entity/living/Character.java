package com.builtbroken.tabletop.game.entity.living;

import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.controller.Controller;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class Character extends Entity
{
    String displayName;

    Controller controller;

    public Character(String name)
    {
        super("character");
        this.displayName = name;
    }
}
