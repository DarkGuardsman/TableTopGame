package com.builtbroken.tabletop.game.map;

/**
 * A small area of the {@link com.builtbroken.tabletop.game.world.World} defined by the {@link MapData}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/15/2017.
 */
public class Section
{
    public final int x, y;
    private int[] tiles;

    /**
     * Creates a new section
     *
     * @param x - location in {@link Grid}
     * @param y - location in {@link Grid}
     */
    public Section(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Tile getTile(int x, int y)
    {
        if (tiles != null)
        {
            return Tile.getFromID(tiles[x + y * 16]);
        }
        return Tiles.AIR;
    }

    public void setTile(Tile tile, int x, int y)
    {
        if (tiles == null)
        {
            tiles = new int[16 * 16];
        }
        tiles[x + y * 16] = tile.id;
    }
}
