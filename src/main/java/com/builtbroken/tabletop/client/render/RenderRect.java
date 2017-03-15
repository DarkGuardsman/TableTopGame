package com.builtbroken.tabletop.client.render;

import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.graphics.Texture;
import com.builtbroken.tabletop.client.graphics.VertexArray;
import com.builtbroken.tabletop.util.Matrix4f;
import com.builtbroken.tabletop.util.Vector3f;


public class RenderRect
{
    private VertexArray mesh;
    private Texture texture;
    private Shader shader;

    public RenderRect(String texture, Shader shader, float width, float height, float layer)
    {
        this.shader = shader;
        this.mesh = VertexArray.createMeshForSize(width, height, layer);
        this.texture = new Texture(texture);
    }

    public RenderRect(String texture, Shader shader, VertexArray mesh)
    {
        this.shader = shader;
        this.mesh = mesh;
        this.texture = new Texture(texture);
    }

    public void render(Vector3f position, float rot, float scale)
    {
        //Setup
        texture.bind();
        shader.enable();
        if (shader == Shader.CHAR)
        {
            shader.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot)));
        }
        else if (shader == Shader.BACKGROUND)
        {
            shader.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(0f, 0.0f, 0.0f)));
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