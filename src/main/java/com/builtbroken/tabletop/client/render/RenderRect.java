package com.builtbroken.tabletop.client.render;

import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.graphics.Texture;
import com.builtbroken.tabletop.client.graphics.VertexArray;
import com.builtbroken.tabletop.util.Matrix4f;
import com.builtbroken.tabletop.util.Vector3f;


public class RenderRect
{
    public VertexArray mesh;
    public Texture texture;
    public Shader shader;

    private Matrix4f cache;

    private float width, height, layer;

    private boolean allowResize = true;


    public RenderRect(String texture, Shader shader)
    {
        this.shader = shader;
        this.texture = new Texture(texture);
    }

    public RenderRect(String texture, Shader shader, float width, float height, float layer)
    {
        this(texture, shader);
        setSize(width, height, layer);
    }

    public RenderRect(String texture, Shader shader, VertexArray mesh)
    {
        this(texture, shader);
        this.mesh = mesh;
    }

    public void setSize(float width, float height)
    {
        setSize(width, height, layer);
    }

    public void setSize(float width, float height, float layer)
    {
        if(allowResize || mesh == null)
        {
            if (this.mesh != null)
            {
                this.mesh.delete();
            }
            this.width = width;
            this.height = height;
            this.layer = layer;
            this.mesh = VertexArray.createMeshForSize(width, height, layer);
        }
    }

    public void render(Vector3f position, float rot, float scale)
    {
        render(position.x, position.y, position.z, rot, scale);
    }

    public void render(float x, float y, float z, float rot, float scale)
    {
        //Setup
        texture.bind();
        shader.enable();
        if (shader == Shader.CHAR)
        {
            if (cache == null)
            {
                cache = new Matrix4f();
            }
            cache = cache.resetToIdentity().translate(x, y, z);
            shader.setUniformMat4f("ml_matrix", cache);
        }
        else if (shader == Shader.BACKGROUND)
        {
            if (cache == null)
            {
                cache = new Matrix4f().resetToIdentity().translate(0f, 0.0f, 0.0f);
            }
            shader.setUniformMat4f("vw_matrix", cache);
        }

        //Render
        mesh.render(scale);

        //Clean up
        shader.disable();
        texture.unbind();
    }

    @Override
    public String toString()
    {
        return "RenderRect[" + texture + "]";
    }
}
