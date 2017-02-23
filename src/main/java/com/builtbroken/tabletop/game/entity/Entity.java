package com.builtbroken.tabletop.game.entity;

import com.builtbroken.jlib.math.dice.Dice;
import com.builtbroken.tabletop.game.GameObject;
import com.builtbroken.tabletop.game.entity.damage.Damage;
import com.builtbroken.tabletop.game.entity.damage.DamageType;
import com.builtbroken.tabletop.game.entity.damage.HitResult;
import com.builtbroken.tabletop.game.items.weapons.Weapon;

/**
 * Something that is placed in the game world
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class Entity extends GameObject
{
    /** Can the entity take damage from attacks */
    boolean canTakeDamae = true;

    int x = 0;
    int y = 0;
    int z = 0;

    int health = -1;
    int shield = -1;

    public Entity(String uniqueID)
    {
        super("entity." + uniqueID);
    }

    /**
     * Called when the entity is attacked by another entity.
     *
     * @param damage
     * @param glance
     * @return
     */
    public HitResult onAttacked(Damage damage, boolean glance)
    {
        //Ignore all damage if we can not take damage
        if (canTakeDamae)
        {
            //Handle shield
            if (shield > 0)
            {
                //Calculate damage
                int d = damage.damageDice.roll();
                int damageSum = d;

                //Reduce shield
                shield -= damageSum;

                //Get remaining damage
                if (shield < 0)
                {
                    damageSum = -shield;
                }

                //IF less than half shot is glancing
                if (damageSum <= (d / 2))
                {
                    glance = true;
                }
            }

            //TODO consider returning a hit object with the amount of damage taken and armor damage
            int armor = getArmorValue(damage.type);
            if (armor > damage.armorPierce)
            {
                //if armor is over twice the shot is deflected, if glance it is deflected by default
                if (armor * 2 >= damage.armorPierce || glance)
                {
                    return HitResult.DEFLECT;
                }

                //Calculate crit change to pen armor
                int diceRollD20 = new Dice(20).roll();
                if (diceRollD20 < 1 + getCriticalBonus())
                {
                    //Apply normal hit
                    applyDamage(damage.damageDice.roll(), glance, false);
                    return HitResult.DAMAGE;
                }
                return HitResult.NO_DAMAGE;
            }
            else
            {
                //Apply normal hit
                applyDamage(damage.damageDice.roll(), glance, false);

                //Calculate crit hit
                int diceRollD20 = new Dice(20).roll();
                if (diceRollD20 < 1 + getCriticalBonus())
                {
                    //Apply crit hit
                    applyDamage(damage.damageDice.roll(), glance, true);
                }
                return HitResult.DAMAGE;
            }
        }
        return HitResult.IGNORE;
    }

    protected void applyDamage(int damageAmount, boolean glance, boolean crit)
    {
        if (glance)
        {
            damageAmount = damageAmount / 2;
        }
        health -= damageAmount;
        //TODO calculate armor damage (armor - (damage / 2))
        //TODO calculate armor damage ( armor - damage) <- math for crit
        //TODO spread armor damage if not wearing a set
    }

    public void attack(Entity entityToAttack, Weapon weapon)
    {
        //Calculate hit roll
        int diceRollD20 = new Dice(20).roll();
        //Calculate hit ability
        int RHR = getRangedHitAbility() - entityToAttack.getRangedReactionAbility();
        //Hit if the dice roll is less than hit ability
        if (diceRollD20 < RHR)
        {
            //TODO handle result
            weapon.attackEntity(this, entityToAttack, false);
        }
        //Glance hit if dice is close enough
        else if (diceRollD20 + 2 <= RHR)
        {
            weapon.attackEntity(this, entityToAttack, true);
        }
        //TODO consume ammo
    }

    /**
     * Called to get the entities current ability to hit
     * a target with a ranged attack.
     * <p>
     * HR = Shooter Agility +Strength + Marksman
     *
     * @return
     */
    public int getRangedHitAbility()
    {
        return 1;
    }

    /**
     * Called to get the entities current ability to
     * react to a ranged attack in order to avoid.
     * <p>
     * RL = Reaction + bonuses + cover level
     *
     * @return
     */
    public int getRangedReactionAbility()
    {
        return 0;
    }

    /**
     * Gets the armor value for the damage type
     *
     * @param type
     * @return
     */
    public int getArmorValue(DamageType type)
    {
        return 0;
    }

    /**
     * Gets critical chance bonus
     *
     * @return
     */
    public int getCriticalBonus()
    {
        return 0;
    }
}
