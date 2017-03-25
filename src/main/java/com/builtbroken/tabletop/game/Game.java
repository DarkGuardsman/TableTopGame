package com.builtbroken.tabletop.game;

import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.actions.Action;
import com.builtbroken.tabletop.game.entity.actions.ActionMove;
import com.builtbroken.tabletop.game.entity.ai.AI;
import com.builtbroken.tabletop.game.entity.controller.Player;
import com.builtbroken.tabletop.game.entity.living.EntityLiving;
import com.builtbroken.tabletop.game.items.ItemState;
import com.builtbroken.tabletop.game.items.Items;
import com.builtbroken.tabletop.game.items.armor.Armor;
import com.builtbroken.tabletop.game.items.weapons.Weapon;
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
        player = new Player("1", "player 1");
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
        Items.load();

        //Register actions
        Action.addAction(new ActionMove());

        //Create a test map
        StaticMapData map = new StaticMapData();
        map.load();
        getWorld().setMapData(map);


        //Generate some characters to render
        getWorld().getEntities().add(new EntityLiving("bob").setController(player).setPosition(0, 0, 0).setRotation(45));
        //getWorld().getEntities().add(new EntityLiving("joe").setController(player).setPosition(-2, 0, 0).setRotation(0));
        //getWorld().getEntities().add(new EntityLiving("paul").setController(player).setPosition(0, 2, 0).setRotation(-90));
        //getWorld().getEntities().add(new EntityLiving("tim").setController(player).setPosition(0, -2, 0).setRotation(90));

        for (Entity entity : getWorld().getEntities())
        {
            if (entity instanceof EntityLiving)
            {
                ((EntityLiving) entity).actionableItems.add(Items.get("item.medkit"));
                ((EntityLiving) entity).actionableItems.add(Items.get("item.grenade"));
                ((EntityLiving) entity).usableWeapons.add((Weapon) Items.get("item.weapon.handgun"));
                ((EntityLiving) entity).heldItem = Items.get("item.weapon.handgun");


                ((EntityLiving) entity).getArmor().equip(new ItemState(Items.get("item.armor.armor1.head")), Armor.ArmorSlot.HEAD, true);
                ((EntityLiving) entity).getArmor().equip(new ItemState(Items.get("item.armor.armor1.chest")), Armor.ArmorSlot.CHEST, true);
            }
        }

        AI ai = new AI("1", "enemy ai");
        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                int x = random.nextInt(20) - random.nextInt(20);
                int y = random.nextInt(20) - random.nextInt(20);
                if (getWorld().getEntity(x, y, 0) == null)
                {
                    //getWorld().getEntities().add(new EntityLiving("enemy" + i).setController(ai).setPosition(x, y, 0));
                    break;
                }
            }
        }

        ai = new AI("2", "neutral ai");
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                int x = random.nextInt(50) - random.nextInt(50);
                int y = random.nextInt(50) - random.nextInt(50);
                if (getWorld().getEntity(x, y, 0) == null)
                {
                    //getWorld().getEntities().add(new EntityLiving("neutral" + i).setController(ai).setPosition(x, y, 0));
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
