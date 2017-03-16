package com.builtbroken.tabletop.game.map.examples;

import com.builtbroken.tabletop.game.map.MapData;
import com.builtbroken.tabletop.game.tile.Tiles;

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
        int x_size = 1000;
        int y_size = 1000;

        int mx = x_size / 2;
        int my = y_size / 2;

        for (int y = 0; y < y_size; y++)
        {
            int dy = my - y;
            for (int x = 0; x < x_size; x++)
            {
                int dx = mx - x;
                if (x == mx && y == my)
                {
                    setTile(Tiles.METAL, x - mx, y - my, 0);
                }
                else if (x == 0 || y == 0)
                {
                    setTile(Tiles.MAP_EDGE, x - mx, y - my, 0);
                }
                else if (Math.abs(dy) < 5 && Math.abs(dx) < 5)
                {
                    setTile(Tiles.PLANKS, x - mx, y - my, 0);
                }
                else if (Math.abs(dy) < 5 || Math.abs(dx) < 5)
                {
                    setTile(Tiles.METAL, x - mx, y - my, 0);
                }
                else
                {
                    setTile(Tiles.STONE, x - mx, y - my, 0);
                }
            }
        }
    }
}
