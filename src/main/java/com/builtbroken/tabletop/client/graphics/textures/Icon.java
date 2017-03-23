package com.builtbroken.tabletop.client.graphics.textures;


/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2017.
 */
public class Icon implements ITexture
{
    public final Texture data;

    private final float u;
    private final float v;
    private final float u2;
    private final float v2;
    private final float width;
    private final float height;

    public Icon(Texture data, float u, float v, float u2, float v2)
    {
        this.data = data;
        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;
        this.width = u2 - u;
        this.height = v2 - v;
    }

    @Override
    public Texture getTexture()
    {
        return data;
    }

    @Override
    public float getWidth()
    {
        return width;
    }

    @Override
    public float getHeight()
    {
        return height;
    }

    @Override
    public float getU()
    {
        return u;
    }

    @Override
    public float getV()
    {
        return v;
    }

    @Override
    public float getU2()
    {
        return u2;
    }

    @Override
    public float getV2()
    {
        return v2;
    }
}
