package com.builtbroken.tabletop.client.graphics.render;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.game.tile.Tile;
import com.builtbroken.tabletop.game.tile.Tiles;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/15/2017.
 */
public class TileRender
{
    public static RenderRect[] renders;

    public static void load()
    {
        renders = new RenderRect[Tiles.TILES.length];
        for (Tile tile : Tiles.TILES)
        {
            if (tile == null)
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
        render(tile, x, y, GameDisplay.TILE_LAYER, scale);
    }

    public static void render(Tile tile, float x, float y, float z, float scale)
    {
        render(tile, x, y, z, 0, scale);
    }

    public static void render(Tile tile, float x, float y, float z, float rot, float scale)
    {
        RenderRect rect = renders[tile.id];
        if (rect != null)
        {
            rect.render(x, y, z, rot, scale);
        }
    }
}
