package com.builtbroken.tabletop.client.tile;

import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.render.RenderRect;
import com.builtbroken.tabletop.game.map.Tile;
import com.builtbroken.tabletop.game.map.Tiles;
import com.builtbroken.tabletop.util.Vector3f;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/15/2017.
 */
public class TileRenders
{
    public static RenderRect[] renders;

    public static void load()
    {
        renders = new RenderRect[Tile.TILES.length];
        for (Tile tile : Tile.TILES)
        {
            if (tile != Tiles.AIR)
            {
                renders[tile.id] = new RenderRect("resources/textures/tiles/" + tile.name + ".png", Shader.CHAR, 1, 1, 0.1f);
            }
        }
    }

    public static void render(Tile tile, float x, float y, float scale)
    {
        renders[tile.id].render(new Vector3f(x, y, 0), 0, scale);
    }
}
