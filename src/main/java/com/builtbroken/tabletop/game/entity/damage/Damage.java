package com.builtbroken.tabletop.game.entity.damage;

import com.builtbroken.jlib.math.dice.Dice;
import com.builtbroken.tabletop.game.entity.Entity;

/**
 * Object used to transfer damage between entities
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public class Damage implements Cloneable
{
    /** Type of damage being applied */
    public final DamageType type;
    /** Dice to use for calculating damage */
    public final Dice damageDice;
    /**
     * How many points of armor the shot can overcome.
     * <p>
     * TODO check if armor pen does more damage than no pen
     * <p>
     * If pen value is equal or greater than armor value
     * the attack will do damage. If the pen value is less
     * than the armor than a critical hit is required. However,
     * the armor takes damage when hit from the shot.
     * <p>
     * If pen value is half the amount of armor no
     * damage is applied. As well the armor takes no
     * damage from the shot.
     */
    public final int armorPierce;
    /** Source of the damage */
    public Entity attacker;

    /**
     * Default constructor
     *
     * @param type - type of damage
     */
    public Damage(DamageType type, Dice damageDice, int armorPierce)
    {
        this.type = type;
        this.damageDice = damageDice;
        this.armorPierce = armorPierce;
    }

    public Damage(Entity entity, DamageType type, Dice damageDice, int armorPierce)
    {
        this(type, damageDice, armorPierce);
        this.attacker = entity;
    }

    @Override
    public Damage clone()
    {
        return new Damage(attacker, type, damageDice, armorPierce);
    }

    /**
     * Called to copy the damage but using the attacker
     *
     * @param attacker - entity attacking
     * @return
     */
    public Damage copy(Entity attacker)
    {
        return new Damage(attacker, type, damageDice, armorPierce);
    }

    /**
     * Called to copy the damage but using the
     * attacker and damage sum.
     *
     * @param attacker
     * @param damageDice
     * @return
     */
    public Damage copy(Entity attacker, Dice damageDice)
    {
        return new Damage(attacker, type, damageDice, armorPierce);
    }
}
