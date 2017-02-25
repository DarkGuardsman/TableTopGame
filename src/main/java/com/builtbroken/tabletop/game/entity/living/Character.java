package com.builtbroken.tabletop.game.entity.living;

import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.controller.Controller;
import com.builtbroken.tabletop.game.entity.inventory.Inventory;
import com.builtbroken.tabletop.game.entity.inventory.InventoryArmor;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class Character extends Entity
{
    /** Traits and stats based on the character. Does not include gear, weapons, armor, or upgrades. */
    protected HashMap<String, Integer> ATTRIBUTES = new HashMap();

    /** Armor equipped to the player */
    protected InventoryArmor armor;
    /** Storage inventory for the character */
    protected Inventory inventory;

    /** Display name for the entity */
    protected String displayName;

    protected Controller controller;

    public Character(String name)
    {
        super("character");
        this.displayName = name;
    }
}
