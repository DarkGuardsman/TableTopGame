package com.builtbroken.tabletop.client.graphics;


import com.builtbroken.tabletop.util.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArray
{
    private int vao, vbo, ibo, tbo;
    private int count;

    public static VertexArray createMeshForSize(float size, float layer)
    {
        return new VertexArray(new float[]{
                -size / 2.0f, -size / 2.0f, layer,
                -size / 2.0f, size / 2.0f, layer,
                size / 2.0f, size / 2.0f, layer,
                size / 2.0f, -size / 2.0f, layer
        });
    }

    public static VertexArray createMeshForSize(float width, float height, float layer)
    {
        return new VertexArray(new float[]{
                0.0f, 0.0f, layer,
                0.0f, height, layer,
                width, height, layer,
                width, 0.0f, layer
        });
    }

    public VertexArray(int count)
    {
        this.count = count;
        vao = glGenVertexArrays();
    }

    public VertexArray(float[] vertices)
    {
        this(vertices,
                new byte[]{
                        0, 1, 2,
                        2, 3, 0
                },
                new float[]{
                        0, 1,
                        0, 0,
                        1, 0,
                        1, 1
                });
    }

    public VertexArray(float[] vertices, byte[] indices, float[] textureCoordinates)
    {
        count = indices.length;

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices), GL_STATIC_DRAW);
        glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0);
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

    public void bind()
    {
        glBindVertexArray(vao);
        if (ibo > 0)
        {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        }
    }

    public void unbind()
    {
        if (ibo > 0)
        {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        }

        glBindVertexArray(0);
    }

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

    public void render()
    {
        bind();
        draw();
    }

}
