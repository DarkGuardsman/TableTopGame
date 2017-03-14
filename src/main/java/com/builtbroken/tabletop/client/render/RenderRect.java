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

    public RenderRect(String texture, float width, float height, float layer)
    {
        this.mesh = VertexArray.createMeshForSize(width, height, layer);
        this.texture = new Texture(texture);
    }

    public void render(Vector3f position, float rot)
    {
        Shader.CHAR.enable();
        Shader.CHAR.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot)));
        texture.bind();
        mesh.render();
    }
}
