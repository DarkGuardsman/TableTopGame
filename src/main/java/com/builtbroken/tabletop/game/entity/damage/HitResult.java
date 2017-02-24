package com.builtbroken.tabletop.game.entity.damage;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/22/2017.
 */
public enum HitResult
{
    /** Entity took damage and took crit damage */
    DAMAGE_CRIT,
    /** Entity took damage */
    DAMAGE,
    /** Armor blocked the damage */
    NO_DAMAGE,
    /** Armor deflected the damage completely */
    DEFLECT,
    /** Entity can not take damage */
    IGNORE,
    /** We missed the target */
    MISS,
    /** Shot hit and damaged the cover */
    DAMAGE_COVER,
    /** Shot hit the cover with no damage */
    HIT_COVER
}
