package com.builtbroken.tabletop.game;

import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.actions.Action;
import com.builtbroken.tabletop.game.entity.actions.ActionMove;
import com.builtbroken.tabletop.game.entity.controller.Player;
import com.builtbroken.tabletop.game.entity.living.Character;
import com.builtbroken.tabletop.game.map.examples.StaticMapData;
import com.builtbroken.tabletop.game.tile.Tiles;
import com.builtbroken.tabletop.game.world.World;

import java.util.Iterator;
import java.util.Random;

/**
 * Main logic controller for the running game state
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class Game implements Runnable
{
    private World world;

    private int updates;
    public int prev_updates;

    public boolean running = true;
    public Thread thread;

    public Player player;

    public final Random random = new Random();

    public Game()
    {
        world = new World(this);
        player = new Player();
    }

    public void start()
    {
        running = true;
        thread = new Thread(this, "GameLogic");
        thread.start();
    }

    @Override
    public void run()
    {
        init();
        try
        {
            long lastTime = System.nanoTime();
            double delta = 0.0;
            double ns = 1000000000.0 / 60.0;
            long timer = System.currentTimeMillis();
            while (running)
            {
                //Calculate the amount of time since last tick
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                //Only update 60 times a second
                if (delta >= 1.0)
                {
                    update(delta);
                    updates++;
                    delta--;
                }

                //If 1 second passes reset
                if (System.currentTimeMillis() - timer > 1000)
                {
                    timer += 1000;
                    prev_updates = updates;
                    updates = 0;
                }
            }
        }
        catch (Exception e)
        {
            save(true);
            e.printStackTrace();
        }
    }

    public void update(double delta)
    {
        //Update entities
        Iterator<Entity> it = getWorld().getEntities().iterator();
        while (it.hasNext())
        {
            Entity entity = it.next();
            //Remove entity if dead
            if (!entity.isAlive()) //TODO add check if valid and inside world still
            {
                it.remove();
            }
            else
            {

                entity.update(delta);
            }
        }
    }

    protected void init()
    {
        Tiles.load();

        //Register actions
        Action.addAction(new ActionMove());

        //Create a test map
        StaticMapData map = new StaticMapData();
        map.load();
        getWorld().setMapData(map);


        //Generate some characters to render
        getWorld().getEntities().add(new Character("bob").setController(player).setPosition(2, 0, 0));
        getWorld().getEntities().add(new Character("joe").setController(player).setPosition(-2, 0, 0));
        getWorld().getEntities().add(new Character("paul").setController(player).setPosition(0, 2, 0));
        getWorld().getEntities().add(new Character("tim").setController(player).setPosition(0, -2, 0));

        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                int x = random.nextInt(20) - random.nextInt(20);
                int y = random.nextInt(20) - random.nextInt(20);
                if (getWorld().getEntity(x, y, 0) == null)
                {
                    getWorld().getEntities().add(new Character("tim").setPosition(x, y, 0));
                    break;
                }
            }
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

    }
}
