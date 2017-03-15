package com.builtbroken.tabletop.game.map.examples;

import com.builtbroken.tabletop.game.map.MapData;
import com.builtbroken.tabletop.game.map.Tiles;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/15/2017.
 */
public class StaticMapData extends MapData
{
    public StaticMapData()
    {
        super("staticData", 1);
    }

    @Override
    public void load()
    {
        setTile(Tiles.STONE, 0, -2, 0);
        setTile(Tiles.STONE, 0, -1, 0);
        setTile(Tiles.STONE, 0, 0, 0);
        setTile(Tiles.STONE, 0, 1, 0);
        setTile(Tiles.STONE, 0, 2, 0);
    }
}
