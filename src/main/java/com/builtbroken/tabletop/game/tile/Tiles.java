package com.builtbroken.tabletop.game.tile;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/15/2017.
 */
public class Tiles
{
    public static Tile AIR;
    public static Tile MAP_EDGE;
    public static Tile STONE;
    public static Tile DIRT;
    public static Tile GRASS;
    public static Tile PLANKS;
    public static Tile METAL;
    public static Tile ROAD;


    /** Array of tiles */
    public static Tile[] TILES;
    private static int nextTileID = 0;

    public static void load()
    {
        TILES = new Tile[256];
        Tiles.AIR = newTile("air").setCanWalk(false);
        Tiles.MAP_EDGE = newTile("mapEdge").setCanWalk(false);
        Tiles.STONE = newTile("stone");
        Tiles.DIRT = newTile("grass");
        Tiles.GRASS = newTile("dirt");
        Tiles.PLANKS = newTile("planks");
        Tiles.METAL = newTile("metal");
        Tiles.ROAD = newTile("road");
    }

    private static Tile newTile(String name)
    {
        Tile tile = new Tile(name, nextTileID++);
        TILES[tile.id] = tile;
        return tile;
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
