package com.builtbroken.tabletop.client.graphics.render;

import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.graphics.textures.ITexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2017.
 */
public class Renderer
{
    Shader shader;

    private boolean drawing = false;
    private final int colorLoc;
    private final int texCoordLoc;


    public Renderer()
    {
        shader = Shader.SHADER;
        colorLoc = GL20.glGetAttribLocation(shader.ID, "Color");
        texCoordLoc = GL20.glGetAttribLocation(shader.ID, "TexCoord");
    }

    public void startDrawing()
    {
        drawing = true;
        //GL11.glBegin(GL11.GL_QUADS);
        GL11.glBegin(GL11.GL_TRIANGLES);
    }

    public void draw(ITexture tex, float x, float y, float layer, float rotationRadians, float scale)
    {
        draw(tex, x, y, tex.getWidth() * scale, tex.getHeight() * scale, layer, 0, 0, rotationRadians, tex.getU(), tex.getV(), tex.getU2(), tex.getV2());
    }

    public void draw(ITexture tex, float x, float y, float width, float height, float layer, float rotationRadians, float scale)
    {
        draw(tex, x, y, width * scale, height * scale, layer, 0, 0, rotationRadians, tex.getU(), tex.getV(), tex.getU2(), tex.getV2());
    }

    public void draw(ITexture tex, float x, float y, float width, float height, float layer, float originX, float originY, float rotationRadians, float u, float v, float u2, float v2)
    {
        Color color = Color.red;
        //checkFlush(tex);
        final float r = color.getRed() / 255f;
        final float g = color.getGreen() / 255f;
        final float b = color.getBlue() / 255f;
        final float a = color.getAlpha() / 255f;


        float x1, y1, x2, y2, x3, y3, x4, y4;

        if (rotationRadians != 0)
        {
            float scaleX = 1f;//width/tex.getWidth();
            float scaleY = 1f;//height/tex.getHeight();

            float cx = originX * scaleX;
            float cy = originY * scaleY;

            float p1x = -cx;
            float p1y = -cy;
            float p2x = width - cx;
            float p2y = -cy;
            float p3x = width - cx;
            float p3y = height - cy;
            float p4x = -cx;
            float p4y = height - cy;

            final float cos = (float) Math.cos(rotationRadians);
            final float sin = (float) Math.sin(rotationRadians);

            x1 = x + (cos * p1x - sin * p1y) + cx; // TOP LEFT
            y1 = y + (sin * p1x + cos * p1y) + cy;
            x2 = x + (cos * p2x - sin * p2y) + cx; // TOP RIGHT
            y2 = y + (sin * p2x + cos * p2y) + cy;
            x3 = x + (cos * p3x - sin * p3y) + cx; // BOTTOM RIGHT
            y3 = y + (sin * p3x + cos * p3y) + cy;
            x4 = x + (cos * p4x - sin * p4y) + cx; // BOTTOM LEFT
            y4 = y + (sin * p4x + cos * p4y) + cy;
        }
        else
        {
            x1 = x;
            y1 = y;

            x2 = x + width;
            y2 = y;

            x3 = x + width;
            y3 = y + height;

            x4 = x;
            y4 = y + height;
        }

        // top left, top right, bottom left
        vert(x1, y1, layer, r, g, b, a, u, v);
        vert(x2, y2, layer, r, g, b, a, u2, v);
        vert(x4, y4, layer, r, g, b, a, u, v2);

        // top right, bottom right, bottom left
        vert(x2, y2, layer, r, g, b, a, u2, v);
        vert(x3, y3, layer, r, g, b, a, u2, v2);
        vert(x4, y4, layer, r, g, b, a, u, v2);
    }

    private void vert(float x, float y, float layer, float r, float g, float b, float a, float u, float v)
    {
        GL20.glVertexAttrib4f(colorLoc, r, g, b, a);
        //GL20.glVertexAttrib2f(texCoordLoc, u, v);
        GL11.glVertex3f(x, y, layer);
    }

    public void endDrawing()
    {
        drawing = false;
        GL11.glEnd();
    }
}
