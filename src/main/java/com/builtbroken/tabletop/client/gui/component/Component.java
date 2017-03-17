package com.builtbroken.tabletop.client.gui.component;

import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.render.RenderRect;
import com.builtbroken.tabletop.util.Vector3f;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/17/2017.
 */
public class Component
{
    public boolean isVisible = true;

    public RenderRect background;
    public Vector3f position;

    public Component(){}

    public Component(float width, float height, String texture, Vector3f position)
    {
        background = new RenderRect(texture, Shader.CHAR, width, height, 0.6f);
        this.position = position;
    }

    /**
     * Render the GUI on the screen
     *
     * @param mouseX
     * @param mouseY
     */
    public void render(float mouseX, float mouseY)
    {
        if (isVisible)
        {
            if (background != null)
            {
                background.render(position.x, position.y, position.z, 0, 1);
            }
        }
    }
}
