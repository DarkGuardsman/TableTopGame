package com.builtbroken.tabletop.game.map;

/**
 * Information about a tile
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/15/2017.
 */
public class Tile
{
    public final String name;
    public final String localization;

    public final int id;

    public static Tile[] TILES;

    public static void load()
    {
        TILES = new Tile[3];
        Tiles.AIR = newTiles(new Tile("air", 0));
        Tiles.STONE = newTiles(new Tile("stone", 1));
        Tiles.DIRT = newTiles(new Tile("dirt", 2));
    }

    private static Tile newTiles(Tile tile)
    {
        TILES[tile.id] = tile;
        return tile;
    }

    public Tile(String name, int id)
    {
        this.id = id;
        this.name = name;
        this.localization = "tile." + name + ".name";
    }

    public static Tile getFromID(int tile)
    {
        if (tile > 0 && tile < TILES.length)
        {
            return TILES[tile];
        }
        return Tiles.AIR;
    }
}
