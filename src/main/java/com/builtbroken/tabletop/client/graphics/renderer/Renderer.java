package com.builtbroken.tabletop.client.graphics.renderer;

import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.graphics.textures.ITexture;
import com.builtbroken.tabletop.client.graphics.textures.Texture;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/23/2017.
 */
public class Renderer
{
    public final int floatPerVertex = 9;

    private FloatBuffer vertices;
    private int numVertices;
    private int maxVerts;
    private boolean drawing;

    private Texture texture;

    private VertexArrayObject vao;
    private VertexBufferObject vbo;

    public Renderer(int size)
    {
        maxVerts = size;
        vertices = MemoryUtil.memAllocFloat(size * floatPerVertex);

        vao = new VertexArrayObject();
        vao.bind();

        vbo = new VertexBufferObject();
        vbo.bind(GL_ARRAY_BUFFER);

        long size2 = vertices.capacity() * Float.BYTES;
        vbo.uploadData(GL_ARRAY_BUFFER, size2, GL_DYNAMIC_DRAW);

        specifyVertexAttributes();

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
        if (numVertices + 6 >= maxVerts)
        {
            flush();
        }
        if (texture == null)
        {
            texture = tex.getTexture();
            texture.bind();
        }
        else if (texture != tex.getTexture())
        {
            texture.unbind();
            flush();
            texture = tex.getTexture();
            texture.bind();
        }


        Color color = Color.WHITE;
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
        vertices.put(x).put(y).put(layer).put(r).put(g).put(b).put(a).put(u).put(v);
        numVertices++;
    }

    /**
     * Begin rendering.
     */
    public void begin()
    {
        if (drawing)
        {
            throw new IllegalStateException("Renderer is already drawing!");
        }
        drawing = true;
        numVertices = 0;
    }

    /**
     * End rendering.
     */
    public void end()
    {
        if (!drawing)
        {
            throw new IllegalStateException("Renderer isn't drawing!");
        }
        drawing = false;
        flush();
    }

    /**
     * Flushes the data to the GPU to let it get rendered.
     */
    public void flush()
    {
        if (numVertices > 0)
        {
            vertices.flip();

            if (vao != null)
            {
                vao.bind();
            }
            else
            {
                vbo.bind(GL_ARRAY_BUFFER);
            }
            Shader.SHADER.enable();

            /* Upload the new vertex data */
            vbo.bind(GL_ARRAY_BUFFER);
            vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);

            /* Draw batch */
            glDrawArrays(GL_TRIANGLES, 0, numVertices);

            /* Clear vertex data for next batch */
            vertices.clear();
            numVertices = 0;
        }
    }

    /**
     * Specifies the vertex pointers.
     */
    private void specifyVertexAttributes()
    {
        Shader.SHADER.enable();
        /* Specify Vertex Pointer */
        int posAttrib = 0;
        glEnableVertexAttribArray(posAttrib);
        glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, floatPerVertex * Float.BYTES, 0);

        /* Specify Color Pointer */
        int colAttrib = 1;
        glEnableVertexAttribArray(colAttrib);
        glVertexAttribPointer(colAttrib, 4, GL_FLOAT, false, floatPerVertex * Float.BYTES, 3 * Float.BYTES);

        /* Specify Texture Pointer */
        int texAttrib = 2;
        glEnableVertexAttribArray(texAttrib);
        glVertexAttribPointer(texAttrib, 2, GL_FLOAT, false, floatPerVertex * Float.BYTES, 7 * Float.BYTES);
    }

    public void dispose()
    {
        MemoryUtil.memFree(vertices);

        if (vao != null)
        {
            vao.delete();
        }
        vbo.delete();
    }

}
