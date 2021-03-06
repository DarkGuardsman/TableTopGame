package com.builtbroken.tabletop.client.graphics.mesh;


import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.util.BufferUtils;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * Used to store mesh data
 */
public class Mesh
{
    private static final byte[] DEFAULT_INDICES = new byte[]{
            0, 1, 2,
            2, 3, 0
    };

    private static final float[] DEFULAT_TEXTURE_COORDS = new float[]{
            0, 1,
            0, 0,
            1, 0,
            1, 1
    };

    //Actual render data
    private int vao, vbo, ibo, tbo;
    private int count;

    //Override used during rendering to change the data at the last min
    private float scale_factor = 1f;

    //Unmodified render data
    private final float[] vertices;
    private final byte[] indices;
    private final float[] textureCoordinates;


    /**
     * Creates a mesh of the size
     *
     * @param size  - size
     * @param layer - depth
     * @return mesh
     */
    public static Mesh createMeshForSize(float size, float layer)
    {
        return new Mesh(new float[]{
                -size / 2.0f, -size / 2.0f, layer,
                -size / 2.0f, size / 2.0f, layer,
                size / 2.0f, size / 2.0f, layer,
                size / 2.0f, -size / 2.0f, layer
        });
    }

    /**
     * Creates a mesh of the size
     *
     * @param size  - size
     * @param layer - depth
     * @return mesh
     */
    public static Mesh createMeshForSize(float size, float layer, float[] uv)
    {
        return new Mesh(new float[]{
                -size / 2.0f, -size / 2.0f, layer,
                -size / 2.0f, size / 2.0f, layer,
                size / 2.0f, size / 2.0f, layer,
                size / 2.0f, -size / 2.0f, layer
        }, DEFAULT_INDICES, uv);
    }

    /**
     * Creats a mesh for the given size data
     *
     * @param width  - x
     * @param height - y
     * @param layer  - depth, z
     * @return mesh
     */
    public static Mesh createMeshForSize(float width, float height, float layer)
    {
        return new Mesh(new float[]{
                0.0f, 0.0f, layer,
                0.0f, height, layer,
                width, height, layer,
                width, 0.0f, layer
        });
    }

    public static Mesh createMeshForSize(float width, float height, float layer, float[] UVs)
    {
        return new Mesh(new float[]{
                0.0f, 0.0f, layer,
                0.0f, height, layer,
                width, height, layer,
                width, 0.0f, layer
        }, DEFAULT_INDICES, UVs);
    }

    /**
     * Generates UVs for a texture sheet
     *
     * @param x - start on the sheet between 0 - 1
     * @param y - start on the sheet between 0 - 1
     * @param u - with of texture on sheet between 0 - 1
     * @param v - with of texture on sheet between 0 - 1
     * @return texture coords for a single rectangle
     */
    public static float[] generateUV(float x, float y, float u, float v)
    {
        float vx = x;
        float vy = y + v;

        float vx1 = x;
        float vy1 = y;

        float vx2 = x + v;
        float vy2 = y;

        float vx3 = x + u;
        float vy3 = y + v;

        return new float[]{vx, vy, vx1, vy1, vx2, vy2, vx3, vy3};
    }

    /**
     * Creats a mesh using the vertices
     * <p>
     * Pre-sets indices and texture coords to save time
     *
     * @param vertices
     */
    public Mesh(float[] vertices)
    {
        this(vertices, DEFAULT_INDICES, DEFULAT_TEXTURE_COORDS);
    }

    /**
     * Creates a mesh using the give data
     *
     * @param vertices
     * @param indices
     * @param textureCoordinates
     */
    public Mesh(float[] vertices, byte[] indices, float[] textureCoordinates)
    {
        //Cache in case we need to change data
        this.vertices = vertices;
        this.indices = indices;
        this.textureCoordinates = textureCoordinates;
        generate();
    }

    /**
     * Called to convert the data into usable render info
     */
    protected void generate()
    {
        count = indices.length;

        float[] vertices = new float[this.vertices.length];
        for (int i = 0; i < vertices.length; i++)
        {
            vertices[i] = this.vertices[i] * scale_factor;
        }

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);

        tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(textureCoordinates), GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.TCOORD_ATTRIB, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(Shader.TCOORD_ATTRIB);

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createByteBuffer(indices), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void dispose()
    {
        //TODO recycle data
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(tbo);
        glDeleteBuffers(ibo);
    }

    /**
     * Binds the mesh to be rendered
     */
    public void bind()
    {
        glBindVertexArray(vao);
        if (ibo > 0)
        {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        }
    }

    /**
     * Unbinds the mesh
     */
    public void unbind()
    {
        if (ibo > 0)
        {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        }

        glBindVertexArray(0);
    }

    /**
     * Draws the mesh on screen
     */
    public void draw()
    {
        if (ibo > 0)
        {
            glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0);
        }
        else
        {
            glDrawArrays(GL_TRIANGLES, 0, count);
        }
    }

    /**
     * Called to render the mesh
     *
     * @param scale - default to 1, used to change the mesh size at the last second
     */
    public void render(float scale)
    {
        //Reload data if we need to change scale
        if (Math.abs(scale_factor - scale) > 0.001)
        {
            scale_factor = scale;
            dispose();
            generate();
        }
        bind();
        draw();
        unbind();
    }
}
