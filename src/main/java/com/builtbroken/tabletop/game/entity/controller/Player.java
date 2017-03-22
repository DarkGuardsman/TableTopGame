package com.builtbroken.tabletop.game.entity.controller;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class Player extends Controller
{
    public Player(String id, String displayName)
    {
        super("player." + id, displayName);
    }

    @Override
    public void update(double delta)
    {
    }
}
