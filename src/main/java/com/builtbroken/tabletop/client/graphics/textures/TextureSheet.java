package com.builtbroken.tabletop.client.graphics.textures;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/19/2017.
 */
public class TextureSheet extends Texture
{
    public final int expectedWidth;
    public final int expectedHeight;
    public final int expectedSizeX;
    public final int expectedSizeY;

    public int iconSizeX;
    public int iconSizeY;

    public int rows;
    public int cols;

    public TextureSheet(String path, int width, int height, int sizeX, int sizeY)
    {
        super(path, false);
        this.expectedWidth = width;
        this.expectedHeight = height;
        this.expectedSizeX = sizeX;
        this.expectedSizeY = sizeY;
        load();
    }

    @Override
    protected void load()
    {
        super.load();
        //Scale icon size if needed
        iconSizeX = (int) Math.floor((width / (float)expectedWidth) * expectedSizeX);
        iconSizeY = (int) Math.floor((height / (float)expectedHeight) * expectedSizeY);
        //Calculated number of rows & cols
        rows = width / iconSizeX;
        cols = height / iconSizeY;
    }
}
