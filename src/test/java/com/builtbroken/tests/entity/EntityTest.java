package com.builtbroken.tests.entity;

import com.builtbroken.jlib.math.dice.Dice;
import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.entity.damage.Damage;
import com.builtbroken.tabletop.game.entity.damage.HitResult;
import com.builtbroken.tabletop.game.items.weapons.Weapon;
import junit.framework.TestCase;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/23/2017.
 */
public class EntityTest extends TestCase
{
    public void testInit()
    {
        Entity entity = new Entity("bob");
        assertEquals("entity.bob", entity.uniqueID);
    }

    public void testAttack()
    {
        Entity attacker = new Entity("attacker");
        Entity entity = new Entity("target");

        Weapon weapon = new Weapon("gun");
        weapon.damage = new Damage(null, new Dice(6), 10);

       assertEquals(HitResult.DAMAGE, attacker.attack(entity, weapon, 5));
    }
}
