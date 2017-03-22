package com.builtbroken.tabletop.client.graphics.textures;


import com.builtbroken.tabletop.util.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class Texture
{
    public static HashMap<String, Texture> textures = new HashMap();

    public int width, height;
    protected int texture;

    protected final String path;

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

    protected Texture(String path)
    {
        this(path, true);
    }

    protected Texture(String path, boolean load)
    {
        this.path = path;
        if (load)
        {
            load();
        }
    }

    protected void load()
    {
        int[] pixels = null;
        try
        {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++)
        {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }


    @Override
    public String toString()
    {
        return "Texture[t = " + texture + ", w = " + width + ", h = " + height + ", path = " + path + "]";
    }
}
