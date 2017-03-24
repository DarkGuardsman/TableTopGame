package com.builtbroken.tabletop.client.graphics.textures;

import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2017.
 */
public class TextureLoader
{
    public static HashMap<String, Texture> textures = new HashMap();

    public static Texture get(String path)
    {
        if (textures.containsKey(path))
        {
            return textures.get(path);
        }
        else
        {
            Texture texture = new Texture(path);
            textures.put(path, texture);
            return texture;
        }
    }

    public static ITexture getTexture(String path, int x, int y, int u, int v)
    {
        Texture data = get(path);
        if (data != null)
        {
            return getTexture(data, x, y, u, v);
        }
        throw new RuntimeException("Failed to load texture " + path);
    }

    public static ITexture getTexture(Texture data, int x, int y, int u, int v)
    {
        return new Icon(data, x, y, u, v);
    }

    public static void disposeTextures()
    {
        for (Texture texture : textures.values())
        {
            if (texture != null)
            {
                texture.dispose();
            }
        }
        textures.clear();
    }
}
