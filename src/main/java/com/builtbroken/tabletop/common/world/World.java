package com.builtbroken.tabletop.common.world;

import com.builtbroken.tabletop.common.Game;
import com.builtbroken.tabletop.common.entity.Entity;
import com.builtbroken.tabletop.common.map.MapInstance;

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
    private MapInstance map;
    /** All entities placed into the world (Both living and non-living) */
    private List<Entity> entities;

    public World(Game game)
    {
        this.game = game;
        entities = new ArrayList();
    }

    public List<Entity> getEntities()
    {
        return entities;
    }
}
