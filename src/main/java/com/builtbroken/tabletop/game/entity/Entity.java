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

    protected float x = 0;
    protected float y = 0;
    protected float z = 0;

    /** Current health value, -1 means it was never set */
    private int health = -1;
    /** Current shield value, works like HP, -1 means it was never set */
    private int shield = -1;

    /**
     * Constructor
     *
     * @param uniqueID - entity type ID, use a different value for instance IDs
     */
    public Entity(String uniqueID)
    {
        super("entity." + uniqueID);
    }

    /**
     * Called to update code that
     * needs to run every tick
     * @param delta
     */
    public void update(double delta)
    {

    }

    /**
     * Called to run decision making
     * logic every tick. This should
     * be used by AI code separately
     * from the update loop. This way
     * all entities can be controlled
     * externally.
     */
    public void updateLogic()
    {

    }

    public void setPosition(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void move(float x, float y, float z)
    {
        setPosition(xf() + x, yf() + y, zf() + z);
    }

    public void move(float x, float y)
    {
        move(x, y, 0);
    }

    //==============================================
    // Damage code
    //==============================================

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
                    return HitResult.DEFLECT; //TODO want to bounce for a chance to do another damage
                }

                //Calculate crit change to pen armor
                int diceRollD20 = new Dice(20).roll();
                if (diceRollD20 < 1 + getCriticalBonus())
                {
                    //Apply normal hit
                    applyDamage(damage, damage.damageDice.roll(), glance, false);
                    return HitResult.DAMAGE;
                }
                return HitResult.NO_DAMAGE;
            }
            else
            {
                //Apply normal hit
                applyDamage(damage, damage.damageDice.roll(), glance, false);

                //Calculate crit hit
                int diceRollD20 = new Dice(20).roll();
                if (diceRollD20 < 1 + getCriticalBonus())
                {
                    //Apply crit hit
                    applyDamage(damage, damage.damageDice.roll(), glance, true);
                    return HitResult.DAMAGE_CRIT;
                }
                return HitResult.DAMAGE;
            }
        }
        return HitResult.IGNORE;
    }

    /**
     * Called to apply HP damage to the entity as well the entities
     * armor.
     *
     * @param damage       - damage data, optional
     * @param damageAmount - damage to apply
     * @param glance       - was the damage a glancing hit
     * @param crit         - was the damage a critical hit
     */
    public void applyDamage(Damage damage, int damageAmount, boolean glance, boolean crit)
    {
        if (glance)
        {
            damageAmount = damageAmount / 2;
        }
        health -= damageAmount;
        if (crit)
        {
            damageArmor(damage, damageAmount);
        }
        else
        {
            damageArmor(damage, damageAmount / 2);
        }
    }

    /**
     * Called to damage the entities armor
     *
     * @param damage - damage data, optional
     * @param amount - amount of damage
     */
    public void damageArmor(Damage damage, int amount)
    {
        //TODO spread armor damage if not wearing a set
    }

    //==============================================
    // Actions
    //==============================================

    /**
     * Called to attack the location with the weapon when
     * a specific entity can not be selected.
     *
     * @param x
     * @param y
     * @param z
     * @param weapon
     */
    public void attack(int x, int y, int z, Weapon weapon)
    {
        //TODO get entities at location
        //TODO get direction from self
    }

    /**
     * Called to attack the entity with the weapon
     * <p>
     * Will roll a D20 to calculate hit
     *
     * @param entityToAttack - entity that will be attacked
     * @param weapon         - weapon being used
     */
    public void attack(Entity entityToAttack, Weapon weapon)
    {
        attack(entityToAttack, weapon, new Dice(20).roll());
    }

    /**
     * Called to attack the entity with the weapon
     *
     * @param entityToAttack - entity that will be attacked
     * @param weapon         - weapon being used
     * @param diceRollD20    - value used to check for hit
     */
    public HitResult attack(Entity entityToAttack, Weapon weapon, int diceRollD20)
    {
        //Calculate hit ability
        int RHR = getHitRating(weapon.isRanged()) - entityToAttack.getReactionRating(weapon.isRanged());
        //Hit if the dice roll is less than hit ability
        if (diceRollD20 < RHR)
        {
            //TODO handle result
            return weapon.attackEntity(this, entityToAttack, false);
        }
        //Glance hit if dice is close enough
        else if (diceRollD20 + 2 <= RHR)
        {
            //TODO handle result
            return weapon.attackEntity(this, entityToAttack, true);
        }
        else
        {
            //TODO if entity is on the other side of cover and we miss see if we hit the cover instead
            //TODO cover is directional
            // return HitResult.HIT_COVER;
        }
        return HitResult.MISS;
    }

    //==============================================
    // Property calls
    //==============================================

    /**
     * Called to set the health of the entity
     * <p>
     * Do not call this directly for normal actions.
     * This should only be used to init the entity
     * after it has been loaded into the world.
     *
     * @param value
     */
    public void setHealth(int value)
    {
        this.health = value; //TODO max health check?
    }

    public int getHealth()
    {
        return health;
    }

    /**
     * Called to get the entities current ability to hit
     * a target with a ranged attack.
     * <p>
     * For ranged attacks
     * HR = Shooter Agility +Strength + Marksman
     * <p>
     * For melee attacks
     * HR = Aggressor Stamina + Reaction + Hand to Hand
     *
     * @return
     */
    public int getHitRating(boolean ranged)
    {
        return 20;
    }

    /**
     * Called to get the entities current ability to
     * react to a ranged attack in order to avoid.
     * <p>
     * For ranged reactions
     * RL = Reaction + bonuses + cover level
     * <p>
     * For melee reactions
     * RL = (Reaction + Fortitude / 2 Rnd Up) + Hand to Hand
     *
     * @return
     */
    public int getReactionRating(boolean ranged)
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

    //==============================================
    // Movement and location code
    //==============================================

    public int xi()
    {
        return (int) x;
    }


    public int yi()
    {
        return (int) y;
    }


    public int zi()
    {
        return (int) z;
    }

    public float xf()
    {
        return x;
    }


    public float yf()
    {
        return y;
    }


    public float zf()
    {
        return z;
    }
}
