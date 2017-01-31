package com.builtbroken.tabletop.api;

import com.builtbroken.tabletop.game.world.WorldMap;

import java.util.HashMap;
import java.util.List;

/**
 * Global set of all maps available to the player at runtime
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class Maps
{
    /** All loaded maps */
    HashMap<String, WorldMap> maps = new HashMap();
    /** Category to maps, used to display maps to the user without showing a massive list */
    HashMap<String, List<String>> mapDisplayList = new HashMap();


}
