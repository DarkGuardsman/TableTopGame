package com.builtbroken.tests.map;

import com.builtbroken.tabletop.game.map.MapData;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class MapTest extends TestCase
{
    @Test
    public void testInit()
    {
        MapData map = new MapData("", 1);
    }
}
