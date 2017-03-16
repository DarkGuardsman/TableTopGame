package com.builtbroken.tabletop.game.world;

import com.builtbroken.tabletop.game.Game;
import com.builtbroken.tabletop.game.entity.Entity;
import com.builtbroken.tabletop.game.map.MapData;
import com.builtbroken.tabletop.game.tile.Tile;
import com.builtbroken.tabletop.game.tile.Tiles;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class World
{
    /** Game instance */
    public final Game game;

    /** Current map this would represents */
    private MapData mapData;
    /** All entities placed into the world (Both living and non-living) */
    private List<Entity> entities;

    public World(Game game)
    {
        this.game = game;
        entities = new ArrayList();
    }

    public void setMapData(MapData map)
    {
        this.mapData = map;
    }

    public Tile getTile(int x, int y, int floor)
    {
        if (mapData != null)
        {
            return mapData.getTile(x, y, floor);
        }
        return Tiles.AIR;
    }

    public List<Entity> getEntities()
    {
        return entities;
    }

    public Entity getEntity(int tileX, int tileY, int floor)
    {
        for (Entity entity : getEntities())
        {
            if (entity.xi() == tileX && entity.yi() == tileY && entity.zi() == floor)
            {
                return entity;
            }
        }
        return null;
    }
}
