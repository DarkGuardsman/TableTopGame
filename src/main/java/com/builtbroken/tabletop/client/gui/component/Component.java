package com.builtbroken.tabletop.client.gui.component;

import com.builtbroken.tabletop.client.GameDisplay;
import com.builtbroken.tabletop.client.graphics.Shader;
import com.builtbroken.tabletop.client.render.RenderRect;

/**
 * Section, container, or part of a GUI
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/17/2017.
 */
public class Component
{
    /** Render object for the component background */
    protected RenderRect background;
    /** Toggle to note something has changed in the GUI */
    protected boolean hasChanged = false;

    /** Location of the component, relative to container */
    public float x, y;

    //Is the component visible
    private boolean isVisible = true;

    //Dimensions of the component
    private float width;
    private float height;

    //Previous dimensions, used to check for change
    private float prev_width;
    private float prev_height;

    //Logic for adjusting position on change or resize of display
    private PositionLogic positionLogic = PositionLogic.NOTHING;

    public Component()
    {
    }

    /**
     * Creates a new component
     *
     * @param width   - dimension in the x
     * @param height  - dimension in the y
     * @param texture - path to texture file
     * @param x       - location x, relative to container
     * @param y       - location y, relative to container
     */
    public Component(float width, float height, String texture, float x, float y)
    {
        background = new RenderRect(texture, Shader.CHAR);
        this.setSize(width, height);
        this.setPosition(x, y);
    }

    /**
     * Sets the width and height of the component.
     * <p>
     * Will trigger a resize of the render object
     * if {@link #isVisible()} returns true
     * <p>
     * param width   - dimension in the x
     *
     * @param height - dimension in the y
     */
    public void setSize(float width, float height)
    {
        this.setWidth(width);
        this.setHeight(height);
    }

    /**
     * Sets the position of the component
     * <p>
     * Triggers position update on next tick
     *
     * @param x
     * @param y
     */
    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
        hasChanged = true;
    }

    /**
     * Called each update tick by the display
     *
     * @param display
     */
    public void update(GameDisplay display)
    {
        if (hasChanged)
        {
            hasChanged = false;
            updatePosition(display);
        }
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
        updatePosition(display);
    }

    /**
     * Called to update the position of the component on the display
     *
     * @param display
     */
    public void updatePosition(GameDisplay display)
    {
        //it is assumed the starting point of a component is the bottom left
        if (getPositionLogic() == PositionLogic.FLOAT_LEFT)
        {
            setPosition(display.cameraBoundLeft, display.cameraBoundBottom);
        }
        else if (getPositionLogic() == PositionLogic.FLOAT_RIGHT)
        {
            setPosition(display.cameraBoundRight - getWidth(), display.cameraBoundBottom);
        }
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public void setVisible(boolean visible)
    {
        isVisible = visible;
        hasChanged = true;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
        hasChanged = true;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
        hasChanged = true;
    }

    public PositionLogic getPositionLogic()
    {
        return positionLogic;
    }

    public void setPositionLogic(PositionLogic positionLogic)
    {
        this.positionLogic = positionLogic;
        hasChanged = true;
    }
}
