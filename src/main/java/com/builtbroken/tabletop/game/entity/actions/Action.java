package com.builtbroken.tabletop.game.entity.actions;

import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.world.World;

import java.util.HashMap;

/**
 * Object defining logic for something an entity can do in the world. This can be anything from movement
 * to attacking to healing. It doesn't matter as long as it can be defined.
 * <p>
 * Action objects are global and should not store data related to the entity.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/21/2017.
 */
public class Action
{
    public static final HashMap<String, Action> ACTION_MAP = new HashMap();

    public final String nameID;
    public String displayName;

    public Action(String nameID)
    {
        this.nameID = nameID;
    }

    /**
     * Adds an action to the map of actions for
     * entities to use
     *
     * @param action
     */
    public static void addAction(Action action)
    {
        if (ACTION_MAP.containsKey(action.nameID))
        {
            System.out.println("Action " + ACTION_MAP.get(action.nameID) + " is being overridden " + action);
        }
        ACTION_MAP.put(action.nameID, action);
    }

    /**
     * Gets an action by its string ID
     *
     * @param key
     * @return
     */
    public static Action get(String key)
    {
        return ACTION_MAP.containsKey(key) ? ACTION_MAP.get(key) : null;
    }

    /**
     * Called to do the action
     *
     * @param entity - entity firing the action
     * @param world  - world to fire action inside
     * @param x      - location to fire action
     * @param y      - location to fire action
     * @param floor  - location to fire action
     */
    public void doAction(Entity entity, World world, int x, int y, int floor)
    {

    }

    /**
     * Called to check if the action can be executed at this time.
     *
     * @param entity - entity firing the action
     * @param world  - world to fire action inside
     * @param x      - location to fire action
     * @param y      - location to fire action
     * @param floor  - location to fire action
     */
    public boolean canDoAction(Entity entity, World world, int x, int y, int floor)
    {
        return false;
    }
}
