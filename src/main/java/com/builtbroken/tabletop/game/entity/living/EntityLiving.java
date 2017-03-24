package com.builtbroken.tabletop.game.entity.living;

import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.actions.ActionMove;
import com.builtbroken.tabletop.game.entity.controller.Controller;
import com.builtbroken.tabletop.game.entity.inventory.Inventory;
import com.builtbroken.tabletop.game.entity.inventory.InventoryArmor;
import com.builtbroken.tabletop.game.items.Item;
import com.builtbroken.tabletop.game.items.ItemState;
import com.builtbroken.tabletop.game.items.weapons.Weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class EntityLiving extends Entity
{
    /** Traits and stats based on the character. Does not include gear, weapons, armor, or upgrades. */
    protected HashMap<String, Integer> ATTRIBUTES = new HashMap();

    /** Armor equipped to the player */
    protected InventoryArmor armor;
    /** Storage inventory for the character */
    protected Inventory inventory;
    /** Weapons that can be used */
    public List<Weapon> usableWeapons = new ArrayList();
    /** Items that can be used */
    public List<Item> actionableItems = new ArrayList();

    public Item activeItem;
    public Item activeWeapon;

    /** Display name for the entity */
    protected String displayName;

    /** What is currently controlling this character. */
    protected Controller controller;

    protected int movementPoints = 10;

    public EntityLiving(String name)
    {
        super("character");
        this.displayName = name;
        this.armor = new InventoryArmor(this);
        this.health = 20;
        init(); //TODO move to world load or something
    }

    public void init()
    {
        //Add actions to entity
        actions.add(ActionMove.KEY);
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);
        if (controller != null)
        {
            controller.update(delta);
        }
    }

    public void onEquipmentChanged(ItemState newGear, ItemState oldGear)
    {

    }

    /**
     * Called
     *
     * @return
     */
    public Inventory getInventory()
    {
        if (inventory == null)
        {
            //TODO init
        }
        return inventory;
    }

    public EntityLiving setController(Controller controller)
    {
        this.controller = controller;
        return this;
    }

    public Controller controller()
    {
        return controller;
    }

    @Override
    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public boolean canMove()
    {
        return movementPoints > 0;
    }
}
