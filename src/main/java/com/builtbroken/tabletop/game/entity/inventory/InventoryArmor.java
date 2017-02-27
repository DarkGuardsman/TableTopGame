package com.builtbroken.tabletop.game.entity.inventory;

import com.builtbroken.tabletop.game.entity.living.Character;
import com.builtbroken.tabletop.game.items.ItemState;
import com.builtbroken.tabletop.game.items.armor.Armor;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/25/2017.
 */
public class InventoryArmor
{
    /** Full suit item, covers all slots */
    private ItemState suit;

    /** Individual armor */
    private ItemState head;
    /** Individual armor */
    private ItemState chest;
    /** Individual armor */
    private ItemState legs;
    /** Individual armor */
    private ItemState gloves;
    /** Individual armor */
    private ItemState boots;

    protected Character character;

    public InventoryArmor(Character character)
    {
        this.character = character;
    }

    /**
     * Called to equip the armor and de-equip existing armor in the slot
     *
     * @param state   - armor
     * @param slot    - slot to equip
     * @param replace - should replace existing armor
     * @return true if equiped.
     */
    public boolean equip(ItemState state, Armor.ArmorSlot slot, boolean replace)
    {
        //TODO de-equip armor to player inventory
        if (canEquip(state, slot, replace) && deequip(slot))
        {
            switch (slot)
            {
                case SUIT:
                    this.suit = state;
                    return true;
                case HEAD:
                    this.head = state;
                    return true;
                case CHEST:
                    this.chest = state;
                    return true;
                case LEGS:
                    this.legs = state;
                    return true;
                case GLOVES:
                    this.gloves = state;
                    return true;
                case BOOTS:
                    this.boots = state;
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if the armor can fit on the character's armor rag doll
     *
     * @param state   - armor
     * @param slot    - loction on character
     * @param replace - care we replacing the armor
     * @return true if the item can be equipped
     */
    public boolean canEquip(ItemState state, Armor.ArmorSlot slot, boolean replace)
    {
        //TODO implement race and body checks to ensure armor fits
        switch (slot)
        {
            case SUIT:
                return head == null && chest == null && legs == null && gloves == null && boots == null && (suit == null || replace);
            case HEAD:
                return suit == null && (head == null || replace);
            case CHEST:
                return suit == null && (chest == null || replace);
            case LEGS:
                return suit == null && (legs == null || replace);
            case GLOVES:
                return suit == null && (gloves == null || replace);
            case BOOTS:
                return suit == null && (boots == null || replace);
        }
        return false;
    }

    /**
     * Attempts to de-equip the armor to the character inventory
     *
     * @param slot
     * @return
     */
    public boolean deequip(Armor.ArmorSlot slot)
    {
        //TODO if inventory is null throw out into world
        if (character != null)
        {
            switch (slot)
            {
                case SUIT:
                    if (character.getInventory() == null || character.getInventory().insertItem(suit))
                    {
                        this.suit = null;
                        return true;
                    }
                case HEAD:
                    if (character.getInventory() == null || character.getInventory().insertItem(head))
                    {
                        this.head = null;
                        return true;
                    }
                case CHEST:
                    if (character.getInventory() == null || character.getInventory().insertItem(chest))
                    {
                        this.chest = null;
                        return true;
                    }
                case LEGS:
                    if (character.getInventory() == null || character.getInventory().insertItem(legs))
                    {
                        this.legs = null;
                        return true;
                    }
                case GLOVES:
                    if (character.getInventory() == null || character.getInventory().insertItem(gloves))
                    {
                        this.gloves = null;
                        return true;
                    }
                case BOOTS:
                    if (character.getInventory() == null || character.getInventory().insertItem(boots))
                    {
                        this.boots = null;
                        return true;
                    }
            }
        }
        return false;
    }
}
