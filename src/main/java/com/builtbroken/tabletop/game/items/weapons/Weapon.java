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
    boolean ranged = true;
    /** How many tiles away can the weapon attack */
    public int range = 1;
    /** How many times can the weapon attack before being reloaded/reactivated/recharged etc */
    public int attackLimit = 1;
    /** Damage applied by the weapon */
    public Damage damage;

    public Weapon(String uniqueID, int id)
    {
        super("weapon." + uniqueID, id);
        canBeHeld = true;
    }

    /**
     * Called to do damage to an entity using the weapon
     * <p>
     * Hit or no hit is calculated by the attacking entity
     *
     * @param attacker
     * @param entityHit
     * @param glance
     * @return
     */
    public HitResult attackEntity(Entity attacker, Entity entityHit, boolean glance)
    {
        consumeUse(1);
        return entityHit.onAttacked(damage.copy(attacker), glance);
    }

    public boolean isRanged()
    {
        return ranged;
    }

    public void consumeUse(int i)
    {
        //TODO implement
        //TODO damage weapon

        //TODO consume charge for ranged weapon
    }
}
