package com.builtbroken.tabletop.game.map;

import com.builtbroken.tabletop.game.tile.Tile;
import com.builtbroken.tabletop.game.tile.Tiles;

import java.util.HashMap;

/**
 * Collection of sections of a map
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/15/2017.
 */
public class Grid
{
    private final HashMap<Long, Section> sections = new HashMap();

    public Section getSection(int x, int y)
    {
        int sectionX = x >> 4;
        int sectionY = y >> 4;
        long id = sectionLocationToLong(sectionX, sectionY);
        if (sections.containsKey(id))
        {
            return sections.get(id);
        }
        return null;
    }

    public static long sectionLocationToLong(int x, int y)
    {
        return (long) x & 4294967295L | ((long) y & 4294967295L) << 32;
    }

    public Tile getTile(int x, int y)
    {
        Section section = getSection(x, y);
        if (section != null)
        {
            return section.getTile(x & 15, y & 15);
        }
        return Tiles.AIR;
    }

    public void setTile(Tile tile, int x, int y)
    {
        Section section = getSection(x, y);
        if (section == null)
        {
            section = new Section(x >> 4, y >> 4);
            sections.put(sectionLocationToLong(x >> 4, y >> 4), section);
        }
        section.setTile(tile, x & 15, y & 15);
    }
}
