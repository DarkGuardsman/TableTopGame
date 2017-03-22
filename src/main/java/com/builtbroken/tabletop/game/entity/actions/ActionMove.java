package com.builtbroken.tabletop.game.entity.actions;

import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/21/2017.
 */
public class ActionMove extends Action
{
    public static final String KEY = "move";

    public ActionMove()
    {
        super(KEY);
    }

    @Override
    public boolean doAction(Entity entity, World world, int x, int y, int floor, boolean left)
    {
        if (!left)
        {
            entity.setPosition(x, y, floor);
            //TODO consume movement points
            //TODO check if can move to point
            //TODO pathfind, etc
            return true;
        }
        return false;
    }

    @Override
    public boolean canDoAction(Entity entity, World world, int x, int y, int floor, boolean left)
    {
        return !left && entity.canMove() && world.getEntity(x, y, floor) == null;
    }

    @Override
    public boolean shouldFreeMouse(Entity entity, boolean leftClick, boolean completed)
    {
        return leftClick || !entity.canMove(); //do not free so we can continually move
    }

    @Override
    public boolean doesUseMouse(boolean leftClick)
    {
        return !leftClick;
    }
}
