package com.builtbroken.tabletop.client.gui.component;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.render.RenderRect;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/17/2017.
 */
public class Component
{
    protected RenderRect background;

    public float x, y;

    private boolean isVisible = true;

    private float width;
    private float height;

    private float prev_width;
    private float prev_height;

    private PositionLogic positionLogic = PositionLogic.NOTHING;

    public Component()
    {
    }

    public Component(float width, float height, String texture, float x, float y)
    {
        background = new RenderRect(texture, Shader.CHAR);
        this.setSize(width, height);
        this.setPosition(x, y);
    }

    public void setSize(float width, float height)
    {
        this.setWidth(width);
        this.setHeight(height);
    }

    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Render the GUI on the screen
     *
     * @param mouseX
     * @param mouseY
     */
    public void render(float mouseX, float mouseY)
    {
        render(mouseX, mouseY, 0, 0, 0);
    }

    /**
     * Render the GUI on the screen
     *
     * @param mouseX
     * @param mouseY
     */
    public void render(float mouseX, float mouseY, float xoffset, float yoffset, float zoffset)
    {
        if (isVisible())
        {
            if (background != null)
            {
                //Resize mesh or later init if mesh was not set
                if (prev_height != height || prev_width != width || background.mesh == null)
                {
                    background.setSize(width, height, GameDisplay.GAME_GUI_LAYER);
                    prev_height = height;
                    prev_width = width;
                }
                background.render(x + xoffset, y + yoffset, zoffset, 0, 1);
            }
        }
    }

    /**
     * Called when the game display has resized
     *
     * @param display
     */
    public void onResize(GameDisplay display)
    {

    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public void setVisible(boolean visible)
    {
        isVisible = visible;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public PositionLogic getPositionLogic()
    {
        return positionLogic;
    }

    public void setPositionLogic(PositionLogic positionLogic)
    {
        this.positionLogic = positionLogic;
    }
}
