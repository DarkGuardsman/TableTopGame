package com.builtbroken.tabletop.client.graphics.render;

import com.builtbroken.tabletop.client.graphics.Mesh;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.graphics.textures.TextureSheet;
import com.builtbroken.tabletop.util.Matrix4f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/19/2017.
 */
public class FontRender
{
    TextureSheet sheet;

    private Matrix4f cache;

    private Mesh[] meshs;

    private float size;

    private float layer;

    private HashMap<Character, CharFontData> charMap = new HashMap();
    private CharFontData[] fontData = new CharFontData[256];

    public FontRender(String textureFile, String dataFile, float size, float layer)
    {
        this.layer = layer;
        this.size = size;
        int line = 0;
        try
        {
            //Load data file for font
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String buffer;
    /*
            while ((buffer = reader.readLine()) != null)
            {
                if (line == 0)
                {
                    textureFile = buffer;
                }
                else
                {
                    //Split line into segments
                    //Parse data
                    CharFontData data = new CharFontData();
                    data.c = buffer.charAt(0);
                    data.width = Integer.parseInt(buffer.substring(2, buffer.length()).trim());
                    data.fontSheetIndex = line - 1;

                    //Store data
                    charMap.put(data.c, data);
                    fontData[line - 1] = data;
                }
                line++;
            }
            */
            reader.close();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error reading line " + line, e);
        }

        sheet = new TextureSheet(textureFile, 512, 1024, 32, 32);
        generate();
    }

    protected void generate()
    {
        meshs = new Mesh[256];

        int index = 0;
        line:
        for (int y = 0; y < sheet.cols; y++)
        {
            for (int x = 0; x < sheet.rows; x++)
            {
                float vx = (float) x / (float) sheet.rows;
                float vy = (float) (y + 1) / (float) sheet.cols;

                float vx1 = (float) x / (float) sheet.rows;
                float vy1 = (float) y / (float) sheet.cols;

                float vx2 = (float) (x + 1) / (float) sheet.rows;
                float vy2 = (float) y / (float) sheet.cols;

                float vx3 = (float) (x + 1) / (float) sheet.rows;
                float vy3 = (float) (y + 1) / (float) sheet.cols;

                float[] textureCoords = new float[]{vx, vy, vx1, vy1, vx2, vy2, vx3, vy3};
                meshs[index++] = Mesh.createMeshForSize(this.size, this.size, layer, textureCoords);
                if (index >= 255)
                {
                    break line;
                }
            }
        }
    }

    public void render(String text, float x, float y, float z, float rot, float scale)
    {
        byte[] chars = text.getBytes(Charset.forName("ISO-8859-1"));
        int numChars = chars.length;

        //Setup
        sheet.bind();
        Shader.CHAR.enable();

        if (cache == null)
        {
            cache = new Matrix4f();
        }

        for (int i = 0; i < numChars; i++)
        {
            byte c = chars[i];
            Mesh mesh = meshs[c];
            Shader.CHAR.setUniformMat4f("ml_matrix", cache.resetToIdentity().translate(x + size * scale * i, y, z));

            //Render
            mesh.render(scale);
        }

        //Clean up
        Shader.CHAR.disable();
        sheet.unbind();
    }

    //Data object
    protected final static class CharFontData
    {
        public char c;
        public int fontSheetIndex;
        public int width;

        @Override
        public String toString()
        {
            return "CharFontData[" + c + " " + fontSheetIndex + "]";
        }
    }
}
