package com.builtbroken.tabletop.game.map;

/**
 * Information about a tile
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/15/2017.
 */
public class Tile
{
    public final String renderName;
    public final String name;
    public final String localization;

    public final int id;

    public static Tile[] TILES;

    public static void load()
    {
        TILES = new Tile[2];
        Tiles.AIR = TILES[0] = new Tile("air", 0);
        Tiles.STONE = TILES[1] = new Tile("stone", 1);
    }

    public Tile(String name, int id)
    {
        this.id = id;
        this.name = name;
        this.localization = "tile." + name + ".name";
        this.renderName = "tile." + name;
    }

    public static Tile getFromID(int tile)
    {
        if(tile > 0 && tile < TILES.length)
        {
            return TILES[tile];
        }
        return Tiles.AIR;
    }
}
