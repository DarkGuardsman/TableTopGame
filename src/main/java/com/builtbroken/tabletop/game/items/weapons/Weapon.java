package com.builtbroken.tabletop.game.items.weapons;

import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.damage.Damage;
import com.builtbroken.tabletop.game.entity.damage.HitResult;
import com.builtbroken.tabletop.game.items.Item;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class Weapon extends Item
{
    /** How many tiles away can the weapon attack */
    int range = 1;
    /** How many times can the weapon attack before being reloaded/reactivated/recharged etc */
    int attackLimit = 1;
    /** Damage applied by the weapon */
    Damage damage;

    public Weapon(String uniqueID)
    {
        super("weapon." + uniqueID);
    }

    public HitResult attackEntity(Entity attacker, Entity entityHit, boolean glance)
    {
        return entityHit.onAttacked(damage.copy(attacker), glance);
    }
}
