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
    public void doAction(Entity entity, World world, int x, int y, int floor)
    {
        entity.setPosition(x, y, floor);
    }

    @Override
    public boolean canDoAction(Entity entity, World world, int x, int y, int floor)
    {
        return entity.canMove() && world.getEntity(x, y, floor) == null;
    }
}
