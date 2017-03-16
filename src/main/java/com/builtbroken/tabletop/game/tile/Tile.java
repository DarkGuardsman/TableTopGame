package com.builtbroken.tabletop.game.tile;

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

    private boolean canWalk = true;

    public Tile(String name, int id)
    {
        this.id = id;
        this.name = name;
        this.localization = "tile." + name + ".name";
    }

    public Tile setCanWalk(boolean b)
    {
        this.canWalk = b;
        return this;
    }

    public boolean canWalk()
    {
        return canWalk;
    }

    @Override
    public String toString()
    {
        return "Tile[" + id + ", " + name + "]";
    }
}
