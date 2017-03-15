package com.builtbroken.tabletop.game.map.examples;

import com.builtbroken.tabletop.game.map.MapData;
import com.builtbroken.tabletop.game.map.Tile;
import com.builtbroken.tabletop.game.map.Tiles;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/15/2017.
 */
public class StaticMapData extends MapData
{
    public static final String[] map = new String[]
            {
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSSSSSSSSDDDDSS",
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSSSDDSSSSSSSSS",
                    "SSSSSSSSSSSDDDSSSSSSSS",
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSSSDDDDSSSSSSS",
                    "SSSSSSSSSSSSDDDSSSSSSS",
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSDDDDSSSSSSSSS",
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSSSSSSSSSSSSSS",
                    "SSSSSSSSSSSSSSSSSSSSSS"
            };

    public StaticMapData()
    {
        super("staticData", 1);
    }

    @Override
    public void load()
    {
        int y_size = map.length;
        int x_size = map[0].length();
        for (int y = 0; y < y_size; y++)
        {
            String string = map[y];
            for (int x = 0; x < x_size; x++)
            {
                char c = string.charAt(x);
                Tile tile = null;
                if (c == 'S')
                {
                    tile = Tiles.STONE;
                }
                else if (c == 'D')
                {
                    tile = Tiles.DIRT;
                }

                if (tile != null)
                {
                    setTile(tile, x - 10, y - 10, 0);
                }
            }
        }
    }
}
