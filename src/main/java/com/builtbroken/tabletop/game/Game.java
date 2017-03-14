package com.builtbroken.tabletop.game;

import com.builtbroken.tabletop.game.world.World;

/**
 * Main logic controller for the running game state
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class Game extends Thread
{
    private World world;

    private boolean running = true;

    @Override
    public void run()
    {
        try
        {
            while (running)
            {

            }
        }
        catch (Exception e)
        {
            save(true);
            e.printStackTrace();
        }
    }

    public World getWorld()
    {
        return world;
    }

    public void save(boolean auto)
    {

    }

    public void load(boolean saveCurrent, String saveName)
    {
        world = new World(this);
    }
}
