package com.builtbroken.tabletop.game.map;

import com.builtbroken.tabletop.game.tile.Tile;
import com.builtbroken.tabletop.game.tile.Tiles;

/**
 * Data about how the map looks and functions
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class MapData
{
    public final String name;

    private Grid[] floors;

    public MapData(String name, int floors)
    {
        this.name = name;
        this.floors = new Grid[floors];
    }

    public void load()
    {

    }

    public Tile getTile(int x, int y, int floor)
    {
        if(floor >= 0 && floor < floors.length)
        {
            return floors[floor].getTile(x, y);
        }
        return Tiles.AIR;
    }

    public boolean setTile(Tile tile, int x, int y, int floor)
    {
        if(floor >= 0 && floor < floors.length)
        {
            if(floors[floor] == null)
            {
                floors[floor] = new Grid();
            }
            floors[floor].setTile(tile, x, y);
        }
        return false;
    }
}
