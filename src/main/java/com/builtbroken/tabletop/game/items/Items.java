package com.builtbroken.tabletop.game.items;

import com.builtbroken.tabletop.game.items.armor.Armor;
import com.builtbroken.tabletop.game.items.weapons.Weapon;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/24/2017.
 */
public class Items
{
    public static Item[] ITEMS;
    private static int nextItemID = 0;

    public static Item VOID;

    public static Item MEDKIT;
    public static Item GRENADE;

    public static Armor HEAD;
    public static Armor CHEST;
    public static Armor LEGS;
    public static Armor GLOVES;
    public static Armor BOOTS;

    public static Weapon HANDGUN;
    public static Weapon RIFLE;
    public static Weapon LAUNCHER;

    public static void load()
    {
        ITEMS = new Item[32000];

        VOID = newItem("void");

        MEDKIT = newItem("medkit");
        GRENADE = newItem("grenade");

        HEAD = newArmor("head");
        CHEST = newArmor("chest");
        LEGS = newArmor("legs");
        GLOVES = newArmor("gloves");
        BOOTS = newArmor("boots");

        HANDGUN = newWeapon("handgun");
        RIFLE = newWeapon("rifle");
        LAUNCHER = newWeapon("launcher");
    }

    private static Item newItem(String name)
    {
        Item item = new Item(name, nextItemID++);
        ITEMS[item.ID] = item;
        return item;
    }

    private static Armor newArmor(String name)
    {
        Armor item = new Armor(name, nextItemID++);
        ITEMS[item.ID] = item;
        return item;
    }

    private static Weapon newWeapon(String name)
    {
        Weapon item = new Weapon(name, nextItemID++);
        ITEMS[item.ID] = item;
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
}
