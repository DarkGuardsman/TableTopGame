package com.builtbroken.tabletop.client.tile;

import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.render.RenderRect;
import com.builtbroken.tabletop.game.tile.Tile;
import com.builtbroken.tabletop.game.tile.Tiles;
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
        renders = new RenderRect[Tiles.TILES.length];
        for (Tile tile : Tiles.TILES)
        {
            if(tile == null)
            {
                break;
            }
            if (tile != Tiles.AIR)
            {
                renders[tile.id] = new RenderRect("resources/textures/tiles/" + tile.name + ".png", Shader.CHAR, 1, 1, 0.1f);
            }
        }
    }

    public static void render(Tile tile, float x, float y, float scale)
    {
        render(tile, x, y, 0, scale);
    }

    public static void render(Tile tile, float x, float y, float z, float scale)
    {
        renders[tile.id].render(new Vector3f(x, y, z), 0, scale);
    }
}
