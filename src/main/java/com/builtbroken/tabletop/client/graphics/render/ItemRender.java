package com.builtbroken.tabletop.client.graphics.render;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.game.items.Item;
import com.builtbroken.tabletop.game.items.Items;

import java.io.File;
import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/24/2017.
 */
public class ItemRender
{
    //public static HashMap<Item, ITexture> icons = new HashMap();
    public static HashMap<Item, RenderRect> renders = new HashMap();

    public static void load()
    {
        for (Item item : Items.ITEMS)
        {
            if (item == null)
            {
                break;
            }
            String path = GameDisplay.TEXTURE_PATH + "icons/items/" + item.uniqueID + ".png";
            File file = new File(path);
            if (file.exists())
            {
                renders.put(item, new RenderRect(path, Shader.GLOBAL_SHADER, 1, 1, 0));
            }
            else
            {
                System.err.println("Missing texture " + path);
            }
        }
    }

    public static void render(Item item, float x, float y, float layer, float scale)
    {
        render(item, x, y, layer, 0, scale);
    }

    public static void render(Item item, float x, float y, float layer, float rot, float scale)
    {
        RenderRect rect = renders.get(item);
        if (rect != null)
        {
            rect.render(x, y, layer, rot, scale);
        }
    }

    public static void dispose()
    {
        for (RenderRect renderRect : renders.values())
        {
            if (renderRect != null)
            {
                renderRect.dispose();
            }
        }
    }
}
