package com.builtbroken.tabletop.client.graphics.render;

import com.builtbroken.tabletop.client.graphics.Mesh;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.graphics.textures.TextureSheet;
import com.builtbroken.tabletop.util.Matrix4f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/19/2017.
 */
public class FontRender
{
    TextureSheet sheet;

    private Matrix4f cache;

    private RenderRect background;

    private float size;

    private float layer;

    private CharFontData[] fontData = new CharFontData[256];


    //Font data read in from .csv file
    public int imageWidth;
    public int imageHeight;
    public int cellWidth;
    public int cellHeight;
    public int starChar;

    public String fontName;
    public String textureFile = null;

    public int fontHeight;
    public int fontWidthDefault;
    public int fontGlobalWidthOffset;
    public int fontGlobalXOffset;
    public int fontGlobalYOffset;

    public FontRender(String dataFile, float size, float layer)
    {
        background = new RenderRect("resources/textures/gui/back.png", Shader.CHAR, size, size, layer - 0.05f);
        this.layer = layer;
        this.size = size;
        int line = 0;

        try
        {
            //Load data file for font
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String buffer;

            while ((buffer = reader.readLine()) != null)
            {
                String[] split = buffer.split(",");
                String prop = split[0];
                String value = split[1];
                if (prop.startsWith("Image"))
                {
                    if (prop.contains("file"))
                    {
                        textureFile = value;
                    }
                    else if (prop.contains("Height"))
                    {
                        imageHeight = Integer.parseInt(value);
                    }
                    else if (prop.contains("Width"))
                    {
                        imageWidth = Integer.parseInt(value);
                    }
                }
                else if (prop.startsWith("Font"))
                {
                    if (prop.contains("Name"))
                    {
                        fontName = value;
                    }
                    else if (prop.contains("Height"))
                    {
                        fontHeight = Integer.parseInt(value);
                    }
                    else if (prop.contains("Width"))
                    {
                        fontWidthDefault = Integer.parseInt(value);
                    }
                }
                else if (prop.startsWith("Cell"))
                {
                    if (prop.contains("Height"))
                    {
                        cellHeight = Integer.parseInt(value);
                    }
                    else if (prop.contains("Width"))
                    {
                        cellWidth = Integer.parseInt(value);
                    }
                }
                else if (prop.startsWith("Global"))
                {
                    if (prop.contains("Width Offset"))
                    {
                        fontGlobalWidthOffset = Integer.parseInt(value);
                    }
                    else if (prop.contains("Y Offset"))
                    {
                        fontGlobalYOffset = Integer.parseInt(value);
                    }
                    else if (prop.contains("X Offset"))
                    {
                        fontGlobalXOffset = Integer.parseInt(value);
                    }
                }
                else if (prop.startsWith("Char"))
                {
                    //Split line into segments
                    //Parse data
                    String num = prop.substring(5, prop.length());
                    num = num.substring(0, num.indexOf(' '));
                    byte c = Byte.parseByte(num);

                    CharFontData data;
                    if (c >= 0)
                    {
                        if (fontData[c] != null)
                        {
                            data = fontData[c];
                        }
                        else
                        {
                            data = new CharFontData();
                            data.c = c;
                            fontData[c] = data;
                        }

                        if (prop.contains("Base Width"))
                        {
                            data.width = Integer.parseInt(value);
                        }
                        else if (prop.contains("Width Offset"))
                        {
                            data.widthOffset = Integer.parseInt(value);
                        }
                        else if (prop.contains("X Offset"))
                        {
                            data.widthXOffset = Integer.parseInt(value);
                        }
                        else if (prop.contains("Y Offset"))
                        {
                            data.widthYOffset = Integer.parseInt(value);
                        }
                    }
                }
                line++;
            }
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            new RuntimeException("Failed reading line " + line, e);
        }


        sheet = new TextureSheet(dataFile.substring(0, dataFile.lastIndexOf("/") + 1) + (textureFile != null ? textureFile : fontName + ".png"), imageWidth, imageHeight, cellWidth, cellHeight);
        generate();
    }

    protected void generate()
    {
        int index = 0;
        line:
        for (int y = 0; y < sheet.cols; y++)
        {
            for (int x = 0; x < sheet.rows; x++)
            {
                int width = getWidth((byte) index);
                float widthScaled = width / (float) sheet.width;

                float vx = (float) x / (float) sheet.rows;
                float vy = (float) (y + 1) / (float) sheet.cols;

                float vx1 = (float) x / (float) sheet.rows;
                float vy1 = (float) y / (float) sheet.cols;

                float vx2 = (x / (float) sheet.rows) + widthScaled;
                float vy2 = (float) y / (float) sheet.cols;

                float vx3 = (x / (float) sheet.rows) + widthScaled;
                float vy3 = (float) (y + 1) / (float) sheet.cols;

                float[] textureCoords = new float[]{vx, vy, vx1, vy1, vx2, vy2, vx3, vy3};

                float meshWidth = (width / (float) cellWidth) * this.size;
                if (fontData[index] != null)
                {
                    fontData[index].size = meshWidth;
                    fontData[index].mesh = Mesh.createMeshForSize(meshWidth, this.size, layer, textureCoords);
                }

                //Increment and check for max
                if (index++ >= 255)
                {
                    break line;
                }
            }
        }
    }

    public void render(String text, float x, float y, float z, float rot, float scale)
    {
        render(text, x, y, z, rot, scale, true);
    }

    public void render(String text, float x, float y, float z, float rot, float scale, boolean background)
    {
        byte[] chars = text.getBytes(Charset.forName("ISO-8859-1"));
        int numChars = chars.length;

        //Render background
        float xf = 0;
        if (background)
        {
            this.background.bind();
            for (int i = 0; i < numChars; i++)
            {
                byte c = chars[i];
                this.background.draw(x + xf, y, z, rot, scale);
                float width = getSizeX(c);
                xf += width * scale;
            }
            this.background.unbind();
        }

        //Render chars
        bind();
        xf = 0;
        for (int i = 0; i < numChars; i++)
        {
            byte c = chars[i];
            renderChar(c, x + xf, y, z, rot, scale);
            float width = getSizeX(c);
            xf += width * scale;
        }
        unbind();
    }

    private void bind()
    {
        sheet.bind();
        Shader.CHAR.enable();
    }

    private void renderChar(byte c, float x, float y, float z, float rot, float scale)
    {
        //Get character data
        CharFontData data = fontData[c];
        if (data == null)
        {
            data = fontData['*'];
        }

        //Set position
        if (cache == null)
        {
            cache = new Matrix4f();
        }
        Shader.CHAR.setUniformMat4f("ml_matrix", cache.resetToIdentity().translate(x, y, z));

        //Render mesh
        data.mesh.render(scale);
    }

    private void unbind()
    {
        Shader.CHAR.disable();
        sheet.unbind();
    }

    public int getWidth(byte c)
    {
        if (c >= 0 && c < fontData.length)
        {
            CharFontData data = fontData[c];
            if (data != null && data.width >= 0)
            {
                return data.width;
            }
            else if (fontWidthDefault >= 0)
            {
                return fontWidthDefault;
            }
        }
        return cellWidth;
    }

    public float getSizeX(byte c)
    {
        if (c >= 0 && c < fontData.length)
        {
            CharFontData data = fontData[c];
            if (data != null && data.width >= 0)
            {
                return data.size;
            }
        }
        return size;
    }

    //Data object
    protected final static class CharFontData
    {
        public byte c;
        public int width;
        public int widthOffset;
        public int widthXOffset;
        public int widthYOffset;

        public float size;

        public Mesh mesh;

        @Override
        public String toString()
        {
            return "CharFontData[" + c + " " + width + "]";
        }
    }
}
