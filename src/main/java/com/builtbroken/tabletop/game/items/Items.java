package com.builtbroken.tabletop.game.items;

import com.builtbroken.tabletop.game.items.armor.Armor;
import com.builtbroken.tabletop.game.items.weapons.Weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/24/2017.
 */
public class Items
{
    public static Item[] ITEMS;
    private static int nextItemID = 0;

    public static HashMap<String, List<Armor>> armorSets = new HashMap();
    public static HashMap<String, Item> nameToitem = new HashMap();

    public static Item VOID;


    public static void load()
    {
        ITEMS = new Item[32000];

        VOID = newItem("void");

        newItem("medkit");
        newItem("grenade");

        newArmorSet("armor1");

        newWeapon("handgun");
        newWeapon("rifle");
        newWeapon("launcher");
    }

    public static void newArmorSet(String name)
    {
        List<Armor> list = new ArrayList();
        for (Armor.ArmorSlot slot : Armor.ArmorSlot.values())
        {
            Armor armor = newArmor(name, slot.name().toLowerCase(), slot);
            list.add(armor);
        }
        armorSets.put(name, list);
    }

    private static Item newItem(String name)
    {
        Item item = new Item(name, nextItemID++);
        ITEMS[item.ID] = item;
        nameToitem.put(item.uniqueID, item);
        return item;
    }

    private static Armor newArmor(String set, String name, Armor.ArmorSlot type)
    {
        Armor item = new Armor(set, name, type, nextItemID++);
        ITEMS[item.ID] = item;
        nameToitem.put(item.uniqueID, item);
        return item;
    }

    private static Weapon newWeapon(String name)
    {
        Weapon item = new Weapon(name, nextItemID++);
        ITEMS[item.ID] = item;
        nameToitem.put(item.uniqueID, item);
        return item;
    }

    public static Item getFromID(int id)
    {
        if (id > 0 && id < ITEMS.length)
        {
            return ITEMS[id] != null ? ITEMS[id] : VOID;
        }
        return VOID;
    }

    public static Item get(String name)
    {
        if (!nameToitem.containsKey(name))
        {
            return VOID;
        }
        return nameToitem.get(name);
    }
}
